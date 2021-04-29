package com.lsm.test.flink.distinct;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import com.clearspring.analytics.stream.membership.BloomFilter;
import com.lsm.test.flink.datastream.util.SocketSourceUtils;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/28 19:41
 **/

public class BloomFilterTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Order> dataStream = SocketSourceUtils.buildDataStream(env, "localhost", 111);

        dataStream.keyBy(Order::getUserName).window(TumblingProcessingTimeWindows.of(Time.minutes(1)))
            .aggregate(new AggregateFunction<Order, Tuple2<BloomFilter, Long>, Long>() {

                @Override
                public Tuple2<BloomFilter, Long> createAccumulator() {
                    BloomFilter bloomFilter = new BloomFilter(10000000, 0.5);
                    return Tuple2.of(bloomFilter, 0L);
                }

                @Override
                public Tuple2<BloomFilter, Long> add(Order value, Tuple2<BloomFilter, Long> accumulator) {
                    BloomFilter bloomFilter = accumulator.f0;
                    Long count = accumulator.f1;
                    if (!bloomFilter.isPresent(String.valueOf(value.getOrderId()))) {
                        count++;
                        bloomFilter.add(String.valueOf(value.getOrderId()));
                    }

                    return Tuple2.of(bloomFilter, count);
                }

                @Override
                public Long getResult(Tuple2<BloomFilter, Long> accumulator) {
                    return accumulator.f1;
                }

                @Override
                public Tuple2<BloomFilter, Long> merge(Tuple2<BloomFilter, Long> a, Tuple2<BloomFilter, Long> b) {

                    return Tuple2.of((BloomFilter)a.f0.merge(b.f0), a.f1 + b.f1);
                }
            }, new ProcessWindowFunction<Long, Tuple2<String, Long>, String, TimeWindow>() {
                @Override
                public void process(String key, Context context, Iterable<Long> elements,
                    Collector<Tuple2<String, Long>> out) throws Exception {
                    Long next = elements.iterator().next();

                    out.collect(Tuple2.of(key, next));
                }
            }).print();

        env.execute();

    }
}

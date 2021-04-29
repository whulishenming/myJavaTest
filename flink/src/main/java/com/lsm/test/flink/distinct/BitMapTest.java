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
import org.roaringbitmap.longlong.Roaring64NavigableMap;

import com.lsm.test.flink.datastream.util.SocketSourceUtils;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/29 10:07
 **/

public class BitMapTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Order> dataStream = SocketSourceUtils.buildDataStream(env, "localhost", 111);

        dataStream.keyBy(Order::getUserName).window(TumblingProcessingTimeWindows.of(Time.seconds(30)))
            .aggregate(new AggregateFunction<Order, Roaring64NavigableMap, Long>() {
                @Override
                public Roaring64NavigableMap createAccumulator() {
                    return new Roaring64NavigableMap();
                }

                @Override
                public Roaring64NavigableMap add(Order value, Roaring64NavigableMap accumulator) {
                    accumulator.add(value.getOrderId());
                    return accumulator;
                }

                @Override
                public Long getResult(Roaring64NavigableMap accumulator) {
                    return accumulator.getLongCardinality();
                }

                @Override
                public Roaring64NavigableMap merge(Roaring64NavigableMap a, Roaring64NavigableMap b) {
                    a.and(b);
                    return a;
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

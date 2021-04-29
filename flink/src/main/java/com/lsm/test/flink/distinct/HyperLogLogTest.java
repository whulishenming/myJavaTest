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

import com.lsm.test.flink.datastream.util.SocketSourceUtils;
import com.lsm.test.flink.vo.Order;

import net.agkn.hll.HLL;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/28 13:46
 **/

public class HyperLogLogTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Order> dataStream = SocketSourceUtils.buildDataStream(env, "localhost", 111);

        dataStream.keyBy(Order::getUserName).window(TumblingProcessingTimeWindows.of(Time.minutes(1)))
            .aggregate(new AggregateFunction<Order, HLL, Long>() {

                @Override
                public HLL createAccumulator() {
                    return new HLL(14, 5);
                }

                @Override
                public HLL add(Order value, HLL accumulator) {
                    accumulator.addRaw(value.getOrderId());
                    return accumulator;
                }

                @Override
                public Long getResult(HLL accumulator) {
                    return accumulator.cardinality();
                }

                @Override
                public HLL merge(HLL a, HLL b) {
                    a.union(b);
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

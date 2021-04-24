package com.lsm.test.flink.datastream.window;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.elasticsearch.common.collect.Tuple;

import com.lsm.test.flink.datastream.util.KafkaSourceUtils;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/22 10:55
 **/

public class AggregateFunctionTest_Average {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Order> dataStream = KafkaSourceUtils.buildDataStream(env);

        dataStream.keyBy(Order::getUserName).window(TumblingProcessingTimeWindows.of(Time.seconds(20)))
            .aggregate(new AggregateFunction<Order, Tuple3<String, Double, Long>, Tuple<String, Double>>() {
                @Override
                public Tuple3<String, Double, Long> createAccumulator() {
                    return Tuple3.of("", 0.0d, 0L);
                }

                @Override
                public Tuple3<String, Double, Long> add(Order value, Tuple3<String, Double, Long> accumulator) {
                    double sum = accumulator.f1 + value.getAmount();
                    long count = accumulator.f2 + 1;
                    return Tuple3.of(value.getUserName(), sum, count);
                }

                @Override
                public Tuple<String, Double> getResult(Tuple3<String, Double, Long> accumulator) {
                    double sum = accumulator.f1;
                    long count = accumulator.f2;
                    return Tuple.tuple(accumulator.f0, sum / count);
                }

                @Override
                public Tuple3<String, Double, Long> merge(Tuple3<String, Double, Long> a,
                    Tuple3<String, Double, Long> b) {
                    return Tuple3.of(a.f0, a.f1 + b.f1, a.f2 + b.f2);
                }
            }).print();

        env.execute();
    }
}

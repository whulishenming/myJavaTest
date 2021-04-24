package com.lsm.test.flink.datastream.window;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import com.lsm.test.flink.datastream.util.KafkaSourceUtils;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/22 14:13
 **/

public class AggregateWithProcessWindowFunctionTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Order> dataStream = KafkaSourceUtils.buildDataStream(env);

        DataStream<Tuple3<String, Double, Long>> aggregate =
            dataStream.keyBy(Order::getUserName).window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
                .aggregate(new AggregateFunction<Order, Tuple2<Long, Double>, Double>() {
                    @Override
                    public Tuple2<Long, Double> createAccumulator() {
                        return Tuple2.of(0L, 0.0d);
                    }

                    @Override
                    public Tuple2<Long, Double> add(Order value, Tuple2<Long, Double> accumulator) {
                        double sum = accumulator.f1 + value.getAmount();
                        long count = accumulator.f0 + 1;

                        return Tuple2.of(count, sum);
                    }

                    @Override
                    public Double getResult(Tuple2<Long, Double> accumulator) {
                        return accumulator.f1 / accumulator.f0;
                    }

                    @Override
                    public Tuple2<Long, Double> merge(Tuple2<Long, Double> a, Tuple2<Long, Double> b) {
                        return Tuple2.of(a.f0 + b.f0, a.f1 + b.f1);
                    }
                }, new ProcessWindowFunction<Double, Tuple3<String, Double, Long>, String, TimeWindow>() {
                    @Override
                    public void process(String key, Context context, Iterable<Double> elements,
                        Collector<Tuple3<String, Double, Long>> out) throws Exception {
                        Double ave = elements.iterator().next();

                        out.collect(Tuple3.of(key, ave, context.window().getStart()));
                    }
                });
        aggregate.print();

        env.execute();
    }
}

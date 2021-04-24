package com.lsm.test.flink.datastream.window;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import com.lsm.test.flink.datastream.util.KafkaSourceUtils;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/21 22:00
 **/

public class WindowTest1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Order> dataStream = KafkaSourceUtils.buildDataStream(env);

        dataStream.keyBy(Order::getUserName).window(TumblingProcessingTimeWindows.of(Time.seconds(20)))
            .aggregate(new AggregateFunction<Order, Order, Order>() {
                @Override
                public Order createAccumulator() {
                    return null;
                }

                @Override
                public Order add(Order value, Order accumulator) {
                    if (accumulator == null) {
                        return value;
                    }
                    return value.getAmount() > accumulator.getAmount() ? value : accumulator;
                }

                @Override
                public Order getResult(Order accumulator) {
                    return accumulator;
                }

                @Override
                public Order merge(Order a, Order b) {
                    return null;
                }
            }).print();

        env.execute();

    }
}

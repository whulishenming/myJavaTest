package com.lsm.test.flink.datastream.window;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import com.lsm.test.flink.datastream.util.KafkaSourceUtils;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/22 10:46
 **/

public class ReduceFunctionTest_MaxBy {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Order> dataStream = KafkaSourceUtils.buildDataStream(env);

        dataStream.keyBy(Order::getUserName).window(TumblingProcessingTimeWindows.of(Time.seconds(20)))
            .reduce(new ReduceFunction<Order>() {
                @Override
                public Order reduce(Order value1, Order value2) throws Exception {
                    if (value1 == null) {
                        return value2;
                    }

                    return value1.getAmount() >= value2.getAmount() ? value1 : value2;
                }
            }).print();

        env.execute();

    }
}

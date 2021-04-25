package com.lsm.test.flink.datastream.eventtime;

import java.time.Duration;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import com.lsm.test.flink.datastream.util.KafkaSourceUtils;
import com.lsm.test.flink.vo.Order;
import com.lsm.utils.DateUtils;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/24 12:56
 **/

public class WatermarkTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // watermark
        WatermarkStrategy<Order> watermarkStrategy =
            WatermarkStrategy.<Order>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                .withTimestampAssigner(new SerializableTimestampAssigner<Order>() {
                    @Override
                    public long extractTimestamp(Order order, long recordTimestamp) {
                        return DateUtils.parseToTimeMillis(order.getCreateTime(), DateUtils.YYYYMMDDHHMMSS_FORMATTER);
                    }
                });

        DataStream<Order> dataStream =
            KafkaSourceUtils.buildDataStream(env).assignTimestampsAndWatermarks(watermarkStrategy);

        dataStream.keyBy(Order::getUserName).window(TumblingEventTimeWindows.of(Time.seconds(10))).maxBy("amount")
            .print();

        env.execute();
    }
}

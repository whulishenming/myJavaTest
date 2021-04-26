package com.lsm.test.flink.datastream.eventtime;

import java.time.Duration;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import com.lsm.test.flink.datastream.util.SocketSourceUtils;
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
                        Long timeMillis =
                            DateUtils.parseToTimeMillis(order.getCreateTime(), DateUtils.YYYYMMDDHHMMSS_FORMATTER);
                        System.out.printf("recordTimestamp={%s}, timeMillis={%s}%n", recordTimestamp, timeMillis);
                        return timeMillis;
                    }
                });

        DataStream<Order> dataStream =
            SocketSourceUtils.buildDataStream(env, "localhost", 111).assignTimestampsAndWatermarks(watermarkStrategy);

        dataStream.keyBy(Order::getUserName).process(new KeyedProcessFunction<String, Order, Order>() {
            @Override
            public void processElement(Order order, Context ctx, Collector<Order> out) throws Exception {
                System.out.printf("currentWatermark={%s}%n", ctx.timerService().currentWatermark());
                out.collect(order);
            }
        }).print();

        env.execute();
    }
}

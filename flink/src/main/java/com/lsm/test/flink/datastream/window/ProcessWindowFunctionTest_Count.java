package com.lsm.test.flink.datastream.window;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.IterableUtils;

import com.lsm.test.flink.datastream.util.KafkaSourceUtils;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/22 13:50
 **/

public class ProcessWindowFunctionTest_Count {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Order> dataStream = KafkaSourceUtils.buildDataStream(env);

        dataStream.keyBy(Order::getUserName).window(TumblingProcessingTimeWindows.of(Time.seconds(20)))
            .process(new ProcessWindowFunction<Order, Tuple3<String, Long, Long>, String, TimeWindow>() {
                @Override
                public void process(String key, Context context, Iterable<Order> elements,
                    Collector<Tuple3<String, Long, Long>> out) throws Exception {
                    long start = context.window().getStart();
                    out.collect(Tuple3.of(key, start, IterableUtils.toStream(elements).count()));
                }
            }).print();

        env.execute();

    }
}

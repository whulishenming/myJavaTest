package com.lsm.test.flink.datastream.window;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
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
 * @date 2021/4/22 14:06
 **/

public class ReduceWithProcessWindowFunctionTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Order> dataStream = KafkaSourceUtils.buildDataStream(env);

        DataStream<Tuple2<Long, Order>> reduce = dataStream.keyBy(Order::getUserName)
            .window(TumblingProcessingTimeWindows.of(Time.seconds(10))).reduce(new ReduceFunction<Order>() {
                @Override
                public Order reduce(Order value1, Order value2) throws Exception {
                    if (value1 == null) {
                        return value2;
                    }

                    return value1.getAmount() >= value2.getAmount() ? value1 : value2;
                }
            }, new ProcessWindowFunction<Order, Tuple2<Long, Order>, String, TimeWindow>() {
                @Override
                public void process(String key, Context context, Iterable<Order> elements,
                    Collector<Tuple2<Long, Order>> out) throws Exception {
                    Order max = elements.iterator().next();

                    out.collect(Tuple2.of(context.window().getStart(), max));
                }
            });

        reduce.print();

        env.execute();
    }
}

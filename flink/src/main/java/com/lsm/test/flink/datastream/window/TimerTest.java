package com.lsm.test.flink.datastream.window;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import com.lsm.test.flink.datastream.util.SocketSourceUtils;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/25 17:25
 **/

public class TimerTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().setAutoWatermarkInterval(0);

        DataStream<Order> dataStream = SocketSourceUtils.buildDataStream(env, "localhost", 111);

        dataStream.keyBy(Order::getUserName).process(new MyKeyedProcessFunction()).print();

        env.execute();
    }

    public static class MyKeyedProcessFunction extends KeyedProcessFunction<String, Order, Tuple2<String, Long>> {
        private ValueState<Double> priceState;

        private ValueState<Long> timerState;

        @Override
        public void processElement(Order value, Context ctx, Collector<Tuple2<String, Long>> out) throws Exception {
            System.out.println(ctx.timerService().currentProcessingTime());
            Double lastPrice = priceState.value();
            Long timer = timerState.value();

            if (lastPrice == null) {
                lastPrice = 0.0d;
            }

            if (value.getAmount() > lastPrice && timer == null) {
                long ts = ctx.timerService().currentProcessingTime() + 10 * 1000L;
                ctx.timerService().registerProcessingTimeTimer(ts);
                timerState.update(ts);

            } else if (value.getAmount() < lastPrice && timer != null) {
                timerState.clear();
                ctx.timerService().deleteProcessingTimeTimer(timer);
            }

            priceState.update(value.getAmount());
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            priceState = getRuntimeContext()
                .getState(new ValueStateDescriptor<>("priceState", TypeInformation.of(new TypeHint<Double>() {})));
            timerState = getRuntimeContext()
                .getState(new ValueStateDescriptor<>("timerState", TypeInformation.of(new TypeHint<Long>() {})));
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<Tuple2<String, Long>> out) throws Exception {
            super.onTimer(timestamp, ctx, out);
            out.collect(Tuple2.of(ctx.getCurrentKey(), timestamp));

            timerState.clear();
        }

        @Override
        public void close() throws Exception {
            super.close();
            priceState.clear();
            timerState.clear();

        }
    }

}

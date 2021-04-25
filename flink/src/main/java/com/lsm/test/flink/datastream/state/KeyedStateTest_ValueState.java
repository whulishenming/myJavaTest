package com.lsm.test.flink.datastream.state;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.lsm.test.flink.datastream.util.SocketSourceUtils;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/24 20:33
 **/

public class KeyedStateTest_ValueState {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Order> dataStream = SocketSourceUtils.buildDataStream(env, "localhost", 111);

        dataStream.keyBy(Order::getUserName).map(new AverageMapFunction()).print();

        env.execute();
    }

    public static class AverageMapFunction extends RichMapFunction<Order, Double> {
        private transient ValueState<Tuple2<Long, Double>> average;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            average = getRuntimeContext().getState(
                new ValueStateDescriptor<>("average", TypeInformation.of(new TypeHint<Tuple2<Long, Double>>() {})));
        }

        @Override
        public void close() throws Exception {
            super.close();
            average.clear();
        }

        @Override
        public Double map(Order value) throws Exception {
            Tuple2<Long, Double> tuple2 = average.value();
            if (tuple2 == null) {
                tuple2 = Tuple2.of(0L, 0.0d);

            }
            Long count = tuple2.f0 + 1;
            double sum = tuple2.f1 + value.getAmount();

            average.update(Tuple2.of(count, sum));

            return sum / count;
        }
    }
}

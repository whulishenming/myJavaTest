package com.lsm.test.flink.datastream.state;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.memory.MemoryStateBackend;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import com.lsm.test.flink.datastream.util.SocketSourceUtils;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/25 09:51
 **/

public class KeyedStateTest_ReducingState {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStateBackend(new MemoryStateBackend());

        DataStream<Order> dataStream = SocketSourceUtils.buildDataStream(env, "localhost", 111);

        dataStream.keyBy(Order::getUserName).flatMap(new MaxByFlatMapFunction()).print();

        env.execute();
    }

    public static class MaxByFlatMapFunction extends RichFlatMapFunction<Order, Order> {
        private transient ReducingState<Order> maxBy;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            maxBy = getRuntimeContext()
                .getReducingState(new ReducingStateDescriptor<Order>("maxBy", new ReduceFunction<Order>() {
                    @Override
                    public Order reduce(Order value1, Order value2) throws Exception {
                        if (value1 == null) {
                            return value2;
                        }
                        return value1.getAmount() >= value2.getAmount() ? value1 : value2;
                    }
                }, Order.class));
        }

        @Override
        public void close() throws Exception {
            super.close();
            maxBy.clear();
        }

        @Override
        public void flatMap(Order value, Collector<Order> out) throws Exception {
            maxBy.add(value);

            out.collect(maxBy.get());
        }
    }
}

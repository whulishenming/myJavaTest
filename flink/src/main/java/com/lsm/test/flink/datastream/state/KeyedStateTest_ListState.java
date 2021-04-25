package com.lsm.test.flink.datastream.state;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.memory.MemoryStateBackend;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.apache.flink.util.IterableUtils;

import com.lsm.test.flink.datastream.util.SocketSourceUtils;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/24 20:59
 **/

public class KeyedStateTest_ListState {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStateBackend(new MemoryStateBackend());

        DataStream<Order> dataStream = SocketSourceUtils.buildDataStream(env, "localhost", 111);

        dataStream.keyBy(Order::getUserName).flatMap(new DistinctFlatMapFunction()).print();

        env.execute();
    }

    public static class DistinctFlatMapFunction extends RichFlatMapFunction<Order, Tuple2<String, List<Long>>> {
        private transient ListState<Long> distinct;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            distinct = getRuntimeContext().getListState(new ListStateDescriptor<>("distinct", Long.class));
        }

        @Override
        public void close() throws Exception {
            super.close();
            distinct.clear();
        }

        @Override
        public void flatMap(Order value, Collector<Tuple2<String, List<Long>>> out) throws Exception {
            List<Long> ids = IterableUtils.toStream(distinct.get()).collect(Collectors.toList());

            if (!ids.contains(value.getOrderId())) {
                ids.add(value.getOrderId());
                distinct.update(ids);
            }

            out.collect(Tuple2.of(value.getUserName(), ids));
        }
    }
}

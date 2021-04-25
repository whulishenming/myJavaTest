package com.lsm.test.flink.datastream.state;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.lsm.test.flink.datastream.util.SocketSourceUtils;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/24 19:36
 **/

public class OperatorStateTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStream<Order> dataStream = SocketSourceUtils.buildDataStream(env, "localhost", 111);

        dataStream.map(new MyMapFunction()).print();

        env.execute();
    }

    public static class MyMapFunction implements MapFunction<Order, Long>, CheckpointedFunction {
        private transient long count = 0;

        private transient ListState<Long> countPerPartition;

        @Override
        public Long map(Order value) throws Exception {
            count = count + 1;
            return count;
        }

        @Override
        public void snapshotState(FunctionSnapshotContext context) throws Exception {
            countPerPartition.clear();
            countPerPartition.add(count);
        }

        @Override
        public void initializeState(FunctionInitializationContext context) throws Exception {
            countPerPartition = context.getOperatorStateStore()
                .getListState(new ListStateDescriptor<>("perPartitionCount", Long.class));

            for (Long l : countPerPartition.get()) {
                count = count + l;
            }

        }
    }

}

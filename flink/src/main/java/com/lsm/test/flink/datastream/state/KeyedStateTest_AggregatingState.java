package com.lsm.test.flink.datastream.state;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.AggregatingState;
import org.apache.flink.api.common.state.AggregatingStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
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
 * @date 2021/4/25 10:25
 **/

public class KeyedStateTest_AggregatingState {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStateBackend(new MemoryStateBackend());

        DataStream<Order> dataStream = SocketSourceUtils.buildDataStream(env, "localhost", 111);

        dataStream.keyBy(Order::getUserName).flatMap(new AverageFlatMapFunction()).print();

        env.execute();
    }

    public static class AverageFlatMapFunction extends RichFlatMapFunction<Order, Tuple2<String, Double>> {
        private transient AggregatingState<Order, Tuple2<String, Double>> average;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            average = getRuntimeContext().getAggregatingState(
                new AggregatingStateDescriptor<Order, Tuple3<String, Double, Long>, Tuple2<String, Double>>("average",
                    new AggregateFunction<Order, Tuple3<String, Double, Long>, Tuple2<String, Double>>() {
                        @Override
                        public Tuple3<String, Double, Long> createAccumulator() {
                            return Tuple3.of("", 0.0d, 0L);
                        }

                        @Override
                        public Tuple3<String, Double, Long> add(Order value, Tuple3<String, Double, Long> accumulator) {
                            return Tuple3.of(value.getUserName(), accumulator.f1 + value.getAmount(),
                                accumulator.f2 + 1);
                        }

                        @Override
                        public Tuple2<String, Double> getResult(Tuple3<String, Double, Long> accumulator) {
                            return Tuple2.of(accumulator.f0, accumulator.f1 / accumulator.f2);
                        }

                        @Override
                        public Tuple3<String, Double, Long> merge(Tuple3<String, Double, Long> a,
                            Tuple3<String, Double, Long> b) {
                            return Tuple3.of(a.f0, a.f1 + b.f1, a.f2 + b.f2);
                        }
                    }, TypeInformation.of(new TypeHint<Tuple3<String, Double, Long>>() {})));
        }

        @Override
        public void close() throws Exception {
            super.close();
            average.clear();
        }

        @Override
        public void flatMap(Order value, Collector<Tuple2<String, Double>> out) throws Exception {
            average.add(value);

            out.collect(average.get());
        }
    }
}

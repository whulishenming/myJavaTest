package com.lsm.test.flink.datastream;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.source.FromElementsFunction;

import com.lsm.test.flink.data.Data;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/15 14:19
 **/


public class DataStreamTest {

    public static void main(String[] args) throws Exception{

        // 环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // source
        DataStream<Integer> source = env.addSource(new FromElementsFunction<>(Types.INT.createSerializer(env.getConfig()), Data.dataList), Types.INT);

        // transformations
        DataStream<Integer> ds = source.map(x -> x * 2).keyBy(value -> 1).sum(0);

        // sink
        ds.addSink(new PrintSinkFunction<>());

        // execute
        env.execute();


    }
}

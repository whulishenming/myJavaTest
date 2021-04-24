package com.lsm.test.flink.datastream.wordcount;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/16 21:22
 **/

public class WordCount {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // nc -lk 111

        /* String host = "localhost";
        int port = 111;*/

        // --host localhost --port 11
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String host = parameterTool.get("host");
        int port = parameterTool.getInt("port");

        DataStreamSource<String> input = env.socketTextStream(host, port);

        input.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] split = s.split(" ");

                for (String str : split) {
                    out.collect(Tuple2.of(str, 1));
                }
            }
        }).keyBy(x -> x.f0).sum(1).print();

        env.execute();

    }
}

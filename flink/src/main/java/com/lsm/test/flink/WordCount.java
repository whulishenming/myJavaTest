package com.lsm.test.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import com.lsm.test.flink.data.Data;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/16 20:46¬
 **/
public class WordCount {

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment executionEnvironment = ExecutionEnvironment.getExecutionEnvironment();

        DataSource<String> ds = executionEnvironment.fromCollection(Data.wordList);

        DataSet<Tuple2<String, Integer>> dataSet = ds.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {

            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] split = s.split(" ");

                for (String str : split) {
                    out.collect(Tuple2.of(str, 1));
                }
            }
        }).groupBy(0).sum(1);

        dataSet.print();

        // executionEnvironment.execute();

    }
}

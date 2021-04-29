package com.lsm.test.flink.sql;

import com.lsm.test.flink.datastream.util.SocketSourceUtils;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/28 10:06
 **/

public class SqlTest {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        EnvironmentSettings bes = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();

        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(env, bes);

        streamTableEnvironment.fromDataStream(SocketSourceUtils.buildDataStream(env, "localhost", 111)).select("");
    }
}

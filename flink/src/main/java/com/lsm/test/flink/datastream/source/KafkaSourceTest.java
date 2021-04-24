package com.lsm.test.flink.datastream.source;

import java.util.Properties;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/17 23:22
 **/

public class KafkaSourceTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "testFlinkGroup");
        DataStream<String> dataStream =
            env.addSource(new FlinkKafkaConsumer<>("test", new SimpleStringSchema(), properties).setStartFromEarliest());

        dataStream.print();

        env.execute();
    }
}

package com.lsm.test.flink.datastream.util;

import java.util.Properties;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import com.alibaba.fastjson.JSONObject;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/21 21:45
 **/

public class KafkaSourceUtils {

    public static DataStream<Order> buildDataStream(StreamExecutionEnvironment env) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "testFlinkGroup");

        return env.addSource(new FlinkKafkaConsumer<>("test", new SimpleStringSchema(), properties).setStartFromGroupOffsets())
            .filter(x -> x != null && x.startsWith("{") && x.endsWith("}"))
            .map(x -> JSONObject.parseObject(x, Order.class));
    }
}

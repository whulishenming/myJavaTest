package com.lsm.test.flink.datastream.sink;

import java.util.Properties;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.cassandra.CassandraSink;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import com.alibaba.fastjson.JSONObject;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/23 10:55
 **/

public class CassandraTupleSinkTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "testFlinkGroup");

        DataStream<Tuple4<Long, String, Double, String>> dataStream = env
            .addSource(new FlinkKafkaConsumer<>("test", new SimpleStringSchema(), properties).setStartFromGroupOffsets())
            .filter(x -> x != null && x.startsWith("{") && x.endsWith("}"))
                .map(x -> {
                Order order = JSONObject.parseObject(x, Order.class);
                return Tuple4.of(order.getOrderId(), order.getUserName(), order.getAmount(), order.getCreateTime());
            }).returns(Types.TUPLE(Types.LONG, Types.STRING, Types.DOUBLE, Types.STRING));

        CassandraSink.addSink(dataStream)
            .setQuery(
                "insert into my_keyspace.order_table(order_id, user_name, amount, create_time) VALUES (?, ?, ?, ?);")
            .setHost("localhost", 9042).build();

        env.execute();

    }
}

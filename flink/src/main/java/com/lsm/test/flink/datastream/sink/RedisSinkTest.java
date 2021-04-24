package com.lsm.test.flink.datastream.sink;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

import com.lsm.test.flink.datastream.util.KafkaSourceUtils;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/20 21:59
 **/

public class RedisSinkTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Order> dataStream = KafkaSourceUtils.buildDataStream(env);

        FlinkJedisPoolConfig flinkJedisPoolConfig =
            new FlinkJedisPoolConfig.Builder().setHost("localhost").setPort(6379).build();

        dataStream.addSink(new RedisSink<>(flinkJedisPoolConfig, new RedisMapper<Order>() {
            @Override
            public RedisCommandDescription getCommandDescription() {
                return new RedisCommandDescription(RedisCommand.HSET, "hash_order_table");
            }

            @Override
            public String getKeyFromData(Order order) {
                return String.valueOf(order.getOrderId());
            }

            @Override
            public String getValueFromData(Order order) {
                return order.getCreateTime();
            }
        }));

        env.execute();

    }
}

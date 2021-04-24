package com.lsm.test.flink.datastream.sink;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.cassandra.CassandraSink;
import org.apache.flink.streaming.connectors.cassandra.MapperOptions;

import com.datastax.driver.mapping.Mapper;
import com.lsm.test.flink.datastream.util.KafkaSourceUtils;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/22 19:26
 **/

public class CassandraPojoSinkTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Order> dataStream = KafkaSourceUtils.buildDataStream(env);

        CassandraSink.addSink(dataStream).setHost("localhost", 9042).setMapperOptions(new MapperOptions() {
            @Override
            public Mapper.Option[] getMapperOptions() {
                Mapper.Option.saveNullFields(true);
                return new Mapper.Option[0];
            }
        }).build();

        env.execute();
    }
}

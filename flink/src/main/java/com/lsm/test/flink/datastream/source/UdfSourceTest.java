package com.lsm.test.flink.datastream.source;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/19 19:30
 **/

public class UdfSourceTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.addSource(new UdfKafkaSourceFunction()).print();

        env.execute();

    }

    public static class UdfKafkaSourceFunction extends RichSourceFunction<Map<String, String>> {
        private KafkaConsumer<String, String> kafkaConsumer;

        private boolean stop = false;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            Properties properties = new Properties();
            properties.setProperty("bootstrap.servers", "localhost:9092");
            properties.setProperty("group.id", "testUdfKafkaSourceFunction");
            properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            properties.put("enable.auto.commit", "true");
            properties.put("auto.offset.reset", "earliest");

            kafkaConsumer = new KafkaConsumer<>(properties);
            kafkaConsumer.subscribe(Collections.singletonList("test"));
        }

        @Override
        public void close() throws Exception {
            super.close();
            stop = true;
        }

        @Override
        public void run(SourceContext<Map<String, String>> sourceContext) throws Exception {
            while (!stop) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(3));

                for (ConsumerRecord<String, String> record : records) {
                    String value = record.value();

                    Map<String, String> recordMap =
                        JSONObject.parseObject(value, new TypeReference<Map<String, String>>() {});

                    sourceContext.collect(recordMap);
                }
            }
        }

        @Override
        public void cancel() {
            stop = true;
        }
    }
}

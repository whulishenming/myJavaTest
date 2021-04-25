package com.lsm.test.flink.datastream.util;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.alibaba.fastjson.JSONObject;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/24 20:00
 **/

public class SocketSourceUtils {

    public static DataStream<Order> buildDataStream(StreamExecutionEnvironment env, String host, Integer port) {
        // nc -lk 111
        return env.socketTextStream(host, port).filter(x -> x != null && x.startsWith("{") && x.endsWith("}"))
            .map(x -> JSONObject.parseObject(x, Order.class));
    }
}

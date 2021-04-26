package com.lsm.test.flink.dimensiontable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.lsm.test.flink.datastream.util.SocketSourceUtils;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/26 15:11 loadAll, 每一小时更新一次
 **/

public class LoadAllTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Order> dataStream = SocketSourceUtils.buildDataStream(env, "localhost", 111);

        dataStream.map(new LoadAllElasticsearchMap()).print();

        env.execute();
    }

    public static class LoadAllElasticsearchMap extends RichMapFunction<Order, Order> {
        private RestHighLevelClient client;

        private Map<String, Long> cache;

        private ScheduledExecutorService executorService;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            client = RestClientsUtils.create();

            executorService = Executors.newSingleThreadScheduledExecutor();

            executorService.scheduleWithFixedDelay(() -> {
                Map<String, Long> resultMap = new HashMap<>();
                try {
                    SearchSourceBuilder searchSourceBuilder =
                        new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).size(1000);

                    SearchRequest searchRequest = new SearchRequest("user").source(searchSourceBuilder);
                    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
                    SearchHits hits = searchResponse.getHits();
                    for (SearchHit hit : hits.getHits()) {
                        Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                        resultMap.put((String)sourceAsMap.get("userName"), Long.valueOf(sourceAsMap.get("user_id").toString()));
                    }
                } catch (Exception e) {
                    System.out.println(ExceptionUtils.getStackTrace(e));
                }

                System.out.println("load cache");

                cache = resultMap;

            }, 0, 20, TimeUnit.SECONDS);
        }

        @Override
        public Order map(Order value) throws Exception {
            value.setUserId(cache.get(value.getUserName()));

            return value;
        }
    }

}

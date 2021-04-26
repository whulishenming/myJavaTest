package com.lsm.test.flink.dimensiontable;

import java.util.Map;

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
 * @date 2021/4/26 15:11 直接查询
 **/

public class SyncTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Order> dataStream = SocketSourceUtils.buildDataStream(env, "localhost", 111);

        dataStream.map(new SyncQueryElasticsearchMap()).print();

        env.execute();
    }

    public static class SyncQueryElasticsearchMap extends RichMapFunction<Order, Order> {
        private RestHighLevelClient client;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            client = RestClientsUtils.create();
        }

        @Override
        public Order map(Order value) throws Exception {
            SearchSourceBuilder searchSourceBuilder =
                new SearchSourceBuilder().query(QueryBuilders.termQuery("userName", value.getUserName())).size(1);
            SearchRequest searchRequest = new SearchRequest("user").source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            if (!hits.iterator().hasNext()) {
                return value;
            }

            SearchHit next = hits.iterator().next();
            if (next != null) {
                Map<String, Object> sourceAsMap = next.getSourceAsMap();
                if (sourceAsMap != null) {
                    value.setUserId(Long.parseLong(sourceAsMap.get("user_id").toString()));
                }
            }

            return value;
        }
    }

}

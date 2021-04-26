package com.lsm.test.flink.dimensiontable;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lsm.test.flink.datastream.util.SocketSourceUtils;
import com.lsm.test.flink.vo.Order;
import com.lsm.utils.EmptyUtils;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/26 15:11 异步查询并缓存
 **/

public class AsyncWithLruTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 每个slot都会缓存一份本地数据
        env.setParallelism(1);
        DataStream<Order> dataStream = SocketSourceUtils.buildDataStream(env, "localhost", 111);

        AsyncDataStream.unorderedWait(dataStream, new AsyncWithLruMap(), 3, TimeUnit.SECONDS).print();

        env.execute();
    }

    public static class AsyncWithLruMap extends RichAsyncFunction<Order, Order> {
        private RestHighLevelClient client;

        Cache<String, String> cache = null;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            client = RestClientsUtils.create();
            cache = CacheBuilder.newBuilder().maximumSize(10000).expireAfterWrite(10, TimeUnit.MINUTES).build();
        }

        @Override
        public void asyncInvoke(Order value, ResultFuture<Order> resultFuture) throws Exception {
            String userId = cache.getIfPresent(value.getUserName());

            if (EmptyUtils.isNotEmpty(userId)) {
                value.setUserId(Long.parseLong(userId));
                System.out.println("withCache");
                resultFuture.complete(Collections.singletonList(value));
            } else {
                SearchSourceBuilder searchSourceBuilder =
                    new SearchSourceBuilder().query(QueryBuilders.termQuery("userName", value.getUserName())).size(1);
                SearchRequest searchRequest = new SearchRequest("user").source(searchSourceBuilder);

                client.searchAsync(searchRequest, RequestOptions.DEFAULT, new ActionListener<SearchResponse>() {
                    @Override
                    public void onResponse(SearchResponse searchResponse) {
                        SearchHits hits = searchResponse.getHits();
                        SearchHit next = hits.iterator().next();
                        if (next != null) {
                            Map<String, Object> sourceAsMap = next.getSourceAsMap();
                            if (sourceAsMap != null) {
                                value.setUserId(Long.parseLong(sourceAsMap.get("user_id").toString()));
                                cache.put(value.getUserName(), String.valueOf(value.getUserId()));
                            }
                            System.out.println("withElasticsearch");

                            resultFuture.complete(Collections.singletonList(value));
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.out.println("withError");
                        resultFuture.completeExceptionally(e);
                    }
                });
            }

        }
    }

}

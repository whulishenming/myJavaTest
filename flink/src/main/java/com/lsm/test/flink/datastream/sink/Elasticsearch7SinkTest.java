package com.lsm.test.flink.datastream.sink;

import java.util.Collections;
import java.util.List;

import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.elasticsearch.ActionRequestFailureHandler;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch7.ElasticsearchSink;
import org.apache.flink.streaming.connectors.elasticsearch7.RestClientFactory;
import org.apache.flink.util.ExceptionUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.common.util.concurrent.EsRejectedExecutionException;
import org.elasticsearch.common.xcontent.XContentType;

import com.alibaba.fastjson.JSONObject;
import com.lsm.test.flink.datastream.util.KafkaSourceUtils;
import com.lsm.test.flink.vo.Order;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/21 10:19
 **/

public class Elasticsearch7SinkTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Order> orderStream = KafkaSourceUtils.buildDataStream(env);

        List<HttpHost> httpHosts = Collections.singletonList(new HttpHost("localhost", 9200, "http"));

        ElasticsearchSink.Builder<Order> elasticsearchSinkBuilder =
            new ElasticsearchSink.Builder<>(httpHosts, new ElasticsearchSinkFunction<Order>() {
                @Override
                public void process(Order order, RuntimeContext runtimeContext, RequestIndexer requestIndexer) {
                    requestIndexer.add(
                        Requests.indexRequest("order_index").source(JSONObject.toJSONString(order), XContentType.JSON));
                }
            });

        elasticsearchSinkBuilder.setBulkFlushMaxSizeMb(1);
        elasticsearchSinkBuilder.setBulkFlushInterval(1000);

        elasticsearchSinkBuilder.setRestClientFactory(new RestClientFactory() {
            @Override
            public void configureRestClientBuilder(RestClientBuilder restClientBuilder) {
                restClientBuilder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
                    @Override
                    public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder builder) {
                        builder.setConnectTimeout(10);
                        builder.setConnectionRequestTimeout(10);
                        builder.setSocketTimeout(10);
                        return builder;
                    }
                });
            }
        });

        // es 失败回调
        elasticsearchSinkBuilder.setFailureHandler(new ActionRequestFailureHandler() {
            @Override
            public void onFailure(ActionRequest actionRequest, Throwable throwable, int restStatusCode,
                RequestIndexer requestIndexer) throws Throwable {
                if (ExceptionUtils.findThrowable(throwable, EsRejectedExecutionException.class).isPresent()) {
                    // full queue; re-add document for indexing
                    requestIndexer.add(actionRequest);
                } else if (ExceptionUtils.findThrowable(throwable, ElasticsearchParseException.class).isPresent()) {
                    // malformed document; simply drop request without failing sink
                } else {
                    // for all other failures, fail the sink
                    // here the failure is simply rethrown, but users can also choose to throw custom exceptions
                    throw throwable;
                }

            }
        });

        orderStream.addSink(elasticsearchSinkBuilder.build());

        env.execute();

    }
}

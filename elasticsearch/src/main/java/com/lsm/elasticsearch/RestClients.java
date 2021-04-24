package com.lsm.elasticsearch;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019/11/7 14:13
 **/

public class RestClients {

    public static RestHighLevelClient create() {
        HttpHost httpHost = new HttpHost("127.0.0.1", 9200, "http");

        RestClientBuilder restClientBuilder = RestClient.builder(httpHost);

        restClientBuilder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(10);
            requestConfigBuilder.setConnectionRequestTimeout(10);
            requestConfigBuilder.setSocketTimeout(10);
            return requestConfigBuilder;
        });

        return new RestHighLevelClient(restClientBuilder);
    }

}

package com.lsm.test.flink.dimensiontable;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/26 15:10
 **/

public class RestClientsUtils {
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

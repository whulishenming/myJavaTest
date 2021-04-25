package lsm.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

import com.google.common.base.Charsets;
import com.lsm.utils.EmptyUtils;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/4/21 18:34
 **/

public class AsyncHttpClient {
    public static CloseableHttpAsyncClient getCloseableHttpAsyncClient(int connectTimeOut, int readTimeOut) {
        return HttpAsyncClients.custom().setDefaultRequestConfig(
            RequestConfig.custom().setConnectTimeout(connectTimeOut).setSocketTimeout(readTimeOut).build()).build();
    }

    public static void sendGet(String url, Map<String, String> paramMap, Map<String, String> headerMap) {
        if (!EmptyUtils.isEmpty(paramMap)) {
            url = url + "?";
        }
        StringJoiner joiner = new StringJoiner("&", url, "");

        if (!EmptyUtils.isEmpty(paramMap)) {
            paramMap.forEach((key, value) -> joiner.add(key + "=" + URLEncoder.encode(value)));
        }

        final HttpGet httpGet = new HttpGet(joiner.toString());

        if (!EmptyUtils.isEmpty(headerMap)) {
            headerMap.forEach(httpGet::addHeader);
        }

        CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.createDefault();

        httpAsyncClient.start();
        httpAsyncClient.execute(httpGet, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(final HttpResponse response) {
                if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                    HttpEntity httpEntity = response.getEntity();
                    try {
                        String result = EntityUtils.toString(httpEntity, Charsets.UTF_8.name());
                        System.out.println("收到回复:" + result);
                    } catch (IOException e) {

                    }
                }
                System.out.println("收到回复" + response.getStatusLine());
            }

            @Override
            public void failed(final Exception ex) {
                System.out.println("发生异常" + ":" + ex.getMessage());
            }

            @Override
            public void cancelled() {
                System.out.println("cancelled");
            }
        });

    }

}

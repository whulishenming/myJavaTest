package lsm.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.lsm.utils.EmptyUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019-03-05 14:58
 **/
@Slf4j
public class HttpUtilsV2 {
    public static final String APPLICATION_JSON = "application/json; charset=utf-8";

    public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";

    public static CloseableHttpClient getProxyHttpClient(int connectTimeOut, int readTimeOut) {
        return HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(connectTimeOut)
            .setSocketTimeout(readTimeOut).setProxy(new HttpHost("host", 8080)).build()).build();
    }

    public static String sendPostForm(CloseableHttpClient httpClient, String url, Map<String, String> paramMap) {
        try {
            HttpPost request = new HttpPost(url);
            request.addHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");

            List<NameValuePair> pairs = Lists.newArrayList();
            if (!EmptyUtils.isEmpty(paramMap)) {
                paramMap.forEach((k, v) -> pairs.add(new BasicNameValuePair(k, v)));
            }
            request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            return EntityUtils.toString(entity, "UTF-8");

        } catch (IOException e) {
            log.error("http sendPostForm error caused by Exception ,url={}, param={}", url,
                JSONObject.toJSONString(paramMap), e);
        }

        return null;
    }

    public static String sendPostJson(CloseableHttpClient httpClient, String url, String postJsonString,
        Map<String, String> headerMap) {
        try {
            HttpPost request = new HttpPost(url);

            request.addHeader("content-type", "application/json;charset=UTF-8");
            if (!EmptyUtils.isEmpty(headerMap)) {
                headerMap.forEach(request::addHeader);
            }

            StringEntity params = new StringEntity(postJsonString, "UTF-8");
            request.setEntity(params);

            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            return EntityUtils.toString(entity, "UTF-8");

        } catch (IOException e) {
            log.error("http sendPostJson error caused by Exception ,url={}, param={}", url, postJsonString, e);
        }

        return null;
    }

    public static String sendGet(CloseableHttpClient httpClient, String url, Map<String, String> paramMap,
        Map<String, String> headerMap) {
        try {
            if (!EmptyUtils.isEmpty(paramMap)) {
                url = url + "?";
            }
            StringJoiner joiner = new StringJoiner("&", url, "");

            if (!EmptyUtils.isEmpty(paramMap)) {
                paramMap.forEach((key, value) -> joiner.add(key + "=" + URLEncoder.encode(value)));
            }

            String newUrl = joiner.toString();

            HttpGet httpGet = new HttpGet(newUrl);

            if (!EmptyUtils.isEmpty(headerMap)) {
                headerMap.forEach(httpGet::addHeader);
            }

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();

            return EntityUtils.toString(httpEntity, Charsets.UTF_8.name());
        } catch (Exception e) {
            log.error("http sendPostJson error caused by Exception ,url={}, param={}", url,
                JSONObject.toJSONString(paramMap), e);
        }

        return null;
    }
}

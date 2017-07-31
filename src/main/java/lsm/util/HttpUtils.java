package lsm.util;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.*;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.print.DocFlavor;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by za-lishenming on 2017/5/10.
 */
@Slf4j
public class HttpUtils {
    /**
     * 通过网络url返回InputStream
     *
     * @param path
     * @return
     */
    public static InputStream returnBitMap(String path) {
        URL url = null;
        InputStream is = null;
        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();//利用HttpURLConnection对象,我们可以从网络中获取网页数据.
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream(); //得到网络返回的输入流

        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    /**
     * 上传文件到指定服务器
     *
     * @param url
     * @param encoding
     * @param filename
     * @param bytes
     * @return
     * @throws FileNotFoundException
     */
    public static String postFileToCOSHttp(String url, String encoding, String filename, byte[] bytes) throws FileNotFoundException {
        String responseMsg = "";
        //构造HttpClient的实例
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

        //设置编码格式
        httpClient.getParams().setContentCharset(encoding);
        //构造PostMethod的实例
        PostMethod postMethod = new PostMethod(url);

        try {
            postMethod.setRequestHeader("Content-Type", "multipart/form-data");

            ByteArrayPartSource source = new ByteArrayPartSource(filename, bytes);
            //FilePart：用来上传文件的类
            FilePart fp = new FilePart("filecontent", source, "multipart/form-data", null);
            //StringPart: 用来上传普通的文本参数
            StringPart sp = new StringPart("op", "upload", "ISO-8859-1");
            Part[] parts = {sp, fp};
            //对于MIME类型的请求，httpclient建议全用MulitPartRequestEntity进行包装
            MultipartRequestEntity mre = new MultipartRequestEntity(parts, postMethod.getParams());
            postMethod.setRequestEntity(mre);

            //执行postMethod,调用http接口
            httpClient.executeMethod(postMethod);
            log.info("send http request, url is " + url + ", status code is" + postMethod.getStatusCode());
            responseMsg = postMethod.getResponseBodyAsString();
            if (200 == postMethod.getStatusCode()) {
                //读取内容
                responseMsg = postMethod.getResponseBodyAsString();
            }
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //释放连接
            postMethod.releaseConnection();

        }
        return responseMsg;
    }

    public static String postFileToCOSHttp(String url, File file) throws Exception {
        String responseMsg = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        //设置请求和传输超时时间
//   RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectionRequestTimeout(CommonConstants.connectionTimeout).build();
//   httpPost.setConfig(requestConfig);
        //把文件转换成流对象FileBody
        FileBody bin = new FileBody(file);
        StringBody op = new StringBody("upload", ContentType.create("text/plain", Consts.ISO_8859_1));
        HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("op", op).addPart("fileContent", bin).build();
//        StringEntity reqEntity = new StringEntity(JSONObject.toJSONString(map),"utf-8");
//        reqEntity.setContentType("application/json");
        httpPost.setEntity(reqEntity);
        //发起请求   并返回请求的响应
        CloseableHttpResponse response = httpClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity resEntity = response.getEntity();
            responseMsg = EntityUtils.toString(resEntity);
        } else {
            response.close();
            httpClient.close();
            throw new Exception("http请求失败，错误码为" + response.getStatusLine().getStatusCode());
        }


        response.close();
        httpClient.close();

        return responseMsg;
    }

    public Map<String, String> postFileToFastDFSHttp(String url, File file, String appName, String[] tags, String userId) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        FileBody fileBody = new FileBody(file);
        StringBody appNameBody = new StringBody(appName, ContentType.create("text/plain", Consts.ISO_8859_1));
        StringBody userIdBody = new StringBody(userId, ContentType.create("text/plain", Consts.ISO_8859_1));
       /* String tagsJson = JSONObject.toJSONString(tags);
        StringBody tagBody = new StringBody(tagsJson, ContentType.create("text/plain", Consts.ISO_8859_1));*/
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addPart("appName", appNameBody)
                .addPart("userId", userIdBody)
                .addPart("file", fileBody)
//                .addPart("tag", tagBody)
                .build();


        httpPost.setEntity(reqEntity);
        //发起请求   并返回请求的响应
        CloseableHttpResponse response = httpClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity resEntity = response.getEntity();
            //{"code":200,"value":{"fileUrl":"http://img1.dev.rs.com/g1/M00/00/B8/wKh6y1lbAPaAJC0OAA2tywXRiX8303.png","width":1575.0,"height":696.0,"fileId":199475419918028800}}
            //{"code":200,"value":{"fileUrl":"http://img1.dev.rs.com/g1/M00/00/B7/wKh6yllbBA6AB2ZxACu3o1KeJOU001.pdf","width":0.0,"height":0.0,"fileId":199478744717643776}}
            String responseMsg = EntityUtils.toString(resEntity);
            JSONObject jsonObject = JSONObject.parseObject(responseMsg);
            int code = jsonObject.getIntValue("code");
            if (code == HttpStatus.SC_OK) {
                JSONObject value = jsonObject.getJSONObject("value");
                resultMap.put("fileUrl", value.getString("fileUrl"));
                resultMap.put("fileId", value.getString("fileId"));
                resultMap.put("width", value.getString("width"));
                resultMap.put("height", value.getString("height"));
            }
        } else {
            httpPost.releaseConnection();
            response.close();
            httpClient.close();
            throw new Exception("http请求失败，错误码为" + response.getStatusLine().getStatusCode());
        }

        httpPost.releaseConnection();
        response.close();
        httpClient.close();

        return resultMap;
    }

    @Test
    public void testUploadImage() throws Exception {

      /*  Map<String, String> resultMap = postFileToFastDFSHttp(
                "http://file.dev.rs.com:80/file/public/inside/upload",
                new FileInputStream(new File("C:/Users/shenming.li/Desktop/12.png")),
                "12.png"
        );*/
        Map<String, String> resultMap = postFileToFastDFSHttp("http://file.dev.rs.com:80/file/public/inside/upload", new File("C:/Users/shenming.li/Desktop/12.png"), "p-kf-knowledge", new String[]{"avatar"}, "00000");
        System.out.println(resultMap);

    }

    @Test
    public void testUploadFile() throws Exception {
        Map<String, String> resultMap = postFileToFastDFSHttp("http://file.dev.rs.com:80/file/public/inside/upload", new File("C:/Users/shenming.li/Desktop/mongodb实战.pdf"), "p-kf-knowledge", new String[]{"avatar"}, "00000");

        System.out.println(resultMap);

    }

    @Test
    public void test() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1000; i < 120000; i++) {
            list.add(i);
        }
        String s = JSONObject.toJSONString(list);
        System.out.println(s);
    }
}

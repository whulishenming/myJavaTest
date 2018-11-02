package lsm.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.*;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by za-lishenming on 2017/5/10.
 */
@Slf4j
public class HttpUtils {
    /**
     * 通过网络url返回InputStream
     * @param path
     * @return
     */
    public static InputStream returnBitMap(String path) {
        URL url = null;
        InputStream is =null;
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
     * @param url
     * @param encoding
     * @param filename
     * @param bytes
     * @return
     * @throws FileNotFoundException
     */
    public static String postFileToCOSHttp(String url, String encoding, String filename, byte[] bytes) throws FileNotFoundException {
        String responseMsg="";
        //构造HttpClient的实例
        HttpClient httpClient=new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

        //设置编码格式
        httpClient.getParams().setContentCharset(encoding);
        //构造PostMethod的实例
        PostMethod postMethod=new PostMethod(url);

        try {
            postMethod.setRequestHeader("Content-Type", "multipart/form-data");

            ByteArrayPartSource source=new ByteArrayPartSource(filename, bytes);
            //FilePart：用来上传文件的类
            FilePart fp = new FilePart("filecontent", source, "multipart/form-data", null);
            //StringPart: 用来上传普通的文本参数
            StringPart sp = new StringPart("op", "upload", "ISO-8859-1");
            Part[] parts = { sp, fp };
            //对于MIME类型的请求，httpclient建议全用MulitPartRequestEntity进行包装
            MultipartRequestEntity mre = new MultipartRequestEntity(parts, postMethod.getParams());
            postMethod.setRequestEntity(mre);

            //执行postMethod,调用http接口
            httpClient.executeMethod(postMethod);
            responseMsg = postMethod.getResponseBodyAsString();
            if(200==postMethod.getStatusCode()){
                //读取内容
                responseMsg = postMethod.getResponseBodyAsString();
            }
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //释放连接
            postMethod.releaseConnection();

        }
        return responseMsg;
    }

    public static String postFileToCOSHttp(String url, File file) throws Exception{
        String responseMsg="";
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
        if (response.getStatusLine().getStatusCode() == 200){
            responseMsg = EntityUtils.toString(reqEntity);
        }else{
            response.close();
            httpClient.close();
            throw new Exception("http请求失败，错误码为" + response.getStatusLine().getStatusCode());
        }
        HttpEntity resEntity = response.getEntity();

        response.close();
        httpClient.close();

        return responseMsg;
    }

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }



}

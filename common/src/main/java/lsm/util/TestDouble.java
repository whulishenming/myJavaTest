package lsm.util;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019-03-27 10:28
 **/

public class TestDouble {

    @Test
    public void test() throws Exception{
        Map<String, String> paramMap = new HashMap<>(6);
        paramMap.put("param", "/webapi/afficheData");
        paramMap.put("productLine", String.valueOf(1));
        paramMap.put("location", String.valueOf(4));
        paramMap.put("terminal", String.valueOf(1));
        paramMap.put("channel", String.valueOf(1));
        paramMap.put("fromStation", "PP-Night Market Branch Virak Buntham");

        String busServiceUrl = "http://qiche.ctripcorp.com/service/index.php";

        String response = HttpUtilsV2.sendGet(HttpUtilsV2.getProxyHttpClient(10, 10), busServiceUrl, paramMap, null);

        JSONObject jsonObject = JSONObject.parseObject(response).getJSONObject("data");

    }
}

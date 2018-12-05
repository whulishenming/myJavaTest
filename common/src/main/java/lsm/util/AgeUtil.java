package lsm.util;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by za-lishenming on 2017/5/27.
 */
public class AgeUtil {

    public int getAge(Date dateOfBirth) {
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (dateOfBirth != null) {
            now.setTime(new Date());
            born.setTime(dateOfBirth);
            if (born.after(now)) {
                throw new IllegalArgumentException("生日不能超过当前日期");
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
            int bornDayOfYear = born.get(Calendar.DAY_OF_YEAR);
            if (nowDayOfYear <= bornDayOfYear) {
                age -= 1;
            }
        }
        return age;
    }

    @Test
    public void test() throws ParseException {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date mydate = myFormatter.parse("2013-05-26");
        System.out.println(getAge(mydate));

    }

    @Test
    public void tests() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();

        // {"userToken":{"code":"41","password":"673121","user":"ctrip_kx"},"reqParam":{"":""}}
        Map<String, String> tokenMap = new HashMap<>(3);
        tokenMap.put("code", "41");
        tokenMap.put("password", "673121");
        tokenMap.put("user", "ctrip_kx");

        Map<String, String> reqParamMap = new HashMap<>(1);

        req.add("userToken", JSONObject.toJSONString(tokenMap));
        req.add("reqParam", JSONObject.toJSONString(reqParamMap));


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/application/json");
        HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity(req, headers);
        String result = restTemplate.postForObject("http://datacenter.bus98.cn/distributor/vega/busshift", r, String.class);
        System.out.println(result);
    }
}

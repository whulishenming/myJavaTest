package lsm.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.*;

/**
 * Created by shenming.li on 2017/7/12.
 */
@Slf4j
public class JsonObjectUtils {

    @Test
    public void test() {
        Map<String, String> map = new HashMap<>();
        map.put("{userName}", "test");
        map.put("{date}", "2017年 10月 12日");
        map.put("{jiner}", "1000");
        System.out.println(JSONObject.toJSONString(map));

        System.out.println(JSONObject.toJSONString(Arrays.asList("userName", "amount", "date")));
    }

    @Test
    public void teat2() {
        String str = "{\"clazz\":\"pushHandlerCommonImpl\"，\"method\":\"jumpToActiveDetail\"，\"param\":{\"activeId\":24}}\n";
        Map<String, String> map = JSONObject.parseObject(str, Map.class);
        System.out.println(map);
    }
    
    @Test
    public void test3() {

        String str = "<p><span style=\"font-size:14px;font-family:宋体\">尊敬的</span><span style=\"font-size:14px;font-family:宋体\">${userName}店铺负责人</span><span style=\"font-size:13px;font-family:宋体\">：</span></p><p><span style=\"font-size:13px;font-family:宋体\">&nbsp; <span style=\"font-size:14px;font-family:宋体\">兹贵我双方所签定的租赁合同条款</span><span style=\"font-size:13px;font-family:宋体\">，</span><span style=\"font-size:14px;font-family:宋体\">由于贵方尚有</span>${amount}<span style=\"font-size:14px;font-family:宋体\">费用未缴清</span><span style=\"font-size:13px;font-family:宋体\">，</span><span style=\"font-size:14px;font-family:宋体\">为了能够使你继续享受到我们及时的服务和履行您的义务</span><span style=\"font-size:13px;font-family:宋体\">，</span><span style=\"font-size:14px;font-family:宋体\">现请您于在收到本通知之日起</span><span style=\"font-size:13px;font-family:&#39;Helvetica Neue&#39;\">5</span><span style=\"font-size:14px;font-family: 宋体\">天内</span><span style=\"font-size:13px;font-family: 宋体\">，</span><span style=\"font-size:14px;font-family: 宋体\">至我商场财务结清相应款项。</span></span></p><p><span style=\"font-size:13px;font-family:宋体\"><span style=\"font-size:14px;font-family: 宋体\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ${date}<br/></span></span></p>";
        String replace = str.replace("${userName}", "lsm").replace("${amount}", "1000").replace("${date}", "2017-07-11 21:19");
        System.out.println(replace);
    }

    @Test
    public void test4() {
        String str = "uuuu${test}yyyy";
        String replace = str.replace("${test}", "kaka");
        System.out.println(replace);
    }

    @Test
    public void listToJson() {

        log.info(JSONObject.toJSONString(Arrays.asList("customerName", "amount", "date")));
    }


}

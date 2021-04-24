package lsm;

import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019/9/20 16:38
 **/

public class JsonPathTest {

    @Test
    public void testJsonPath() {
        String json = "{\"action\":\"/interface.service/xxx/queryBlackUserData\",\"all\":\"1\",\"result\":{\"count\":2,\"tenant_count\":2,\"records\":[{\"name\":\"张三\",\"pid\":\"500234199212121212\",\"mobile\":\"18623456789\",\"applied_at\":\"3\",\"confirmed_at\":\"5\",\"confirm_type\":\"overdue\",\"loan_type\":1,\"test\":\"mytest\",\"all\":\"2\"},{\"name\":\"李四\",\"pid\":\"500234199299999999\",\"mobile\":\"13098765432\",\"applied_at\":\"1\",\"confirmed_at\":\"\",\"confirm_type\":\"overdue\",\"loan_type\":3,\"all\":\"3\"},{\"name\":\"王五\",\"pid\":\"50023415464654659\",\"mobile\":\"1706454894\",\"applied_at\":\"-1\",\"confirmed_at\":\"\",\"confirm_type\":\"overdue\",\"loan_type\":3}],\"all\":\"4\"},\"code\":200,\"subtime\":\"1480495123550\",\"status\":\"success\",\"ok\":3}";

        JSONObject jsonObject = JSONObject.parseObject(json);

        // 1 返回所有name ["张三","李四","王五"]
        JSONArray eval1 = (JSONArray) JSONPath.eval(jsonObject, "result.records[*].name");
        List<String> eval1List = eval1.toJavaList(String.class);

        // 2 返回所有数组的值
        Object eval2 = JSONPath.eval(jsonObject, "$.result.records[*]");

        // 3 返回第一个的name
        Object eval3 = JSONPath.eval(jsonObject, "$.result.records[0].name");

        // 4 返回下标为0 和 2 的数组值
        Object eval4 = JSONPath.eval(jsonObject, "$.result.records[0,2].name");

        // 5 返回下标为0 到 下标为1的 的数组值  这里[0:2] 表示包含0 但是 不包含2
        Object eval5 = JSONPath.eval(jsonObject, "$.result.records[0:2].name");

        // 6 返回数组的最后两个值
        Object eval6 = JSONPath.eval(jsonObject, "$.result.records[-2:].name");

        // 7 返回下标为1之后的所有数组值 包含下标为1的
        Object eval7 = JSONPath.eval(jsonObject, "$.result.records[1:].name");

        // 8 返回下标为3之前的所有数组值  不包含下标为3的
        Object eval8 = JSONPath.eval(jsonObject, "$.result.records[:3].name");

        // 9 返回applied_at大于等于2的值
        Object eval9 = JSONPath.eval(jsonObject, "$.result.records[?(@.applied_at >= '2')]");

        // 10 返回name等于李四的值
        Object eval10 = JSONPath.eval(jsonObject, "$.result.records[?(@.name == '李四')]");

        // 11 返回有test属性的数组
        Object eval11 = JSONPath.eval(jsonObject, "$.result.records[?(@.test)]");

        // 12 返回有all属性的所有值
        Object eval12 = JSONPath.eval(jsonObject, "$..all");

        // 13 正则匹配
        Object eval14 = JSONPath.eval(jsonObject, "$.result.records[?(@.pid =~ /.*999/i)]");

        // 14 多条件
        Object eval15 = JSONPath.eval(jsonObject, "$.result.records[?(@.mobile == '1706454894' || @.name == '李四' )].mobile");

        // 15 查询数组长度
        Object eval16 = JSONPath.eval(jsonObject, "$.result.records.length()");

        System.out.println(eval1);
    }
}

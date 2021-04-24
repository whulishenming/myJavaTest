package lsm.redis.test;

import com.alibaba.fastjson.JSONObject;
import lsm.redis.api.Scan;
import org.junit.Test;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/2/4 10:11
 **/

public class ScanTest {
    private Scan scan = new Scan();

    @Test
    public void testScan() {
        ScanResult<String> result = scan.scan("0");
        String stringCursor = result.getStringCursor();
        List<String> keysResult = result.getResult();

        if (Objects.equals(stringCursor, "0")) {
            System.out.println(JSONObject.toJSONString(keysResult));
            System.out.println("over");
        }
        System.out.println(result);
    }

    @Test
    public void testScan2() {
        ScanParams params = new ScanParams();
        params.count(2);
        params.match("test*");

        ScanResult<String> result = scan.scan("0", params);
        Set<String> keysResult = new HashSet<>(result.getResult());

        while (!Objects.equals(result.getStringCursor(), "0")) {
            result = scan.scan(result.getStringCursor(), params);
            keysResult.addAll(result.getResult());
        }

        System.out.println(JSONObject.toJSONString(keysResult));
        System.out.println("over");
    }
}

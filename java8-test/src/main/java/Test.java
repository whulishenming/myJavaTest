import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019-03-26 13:19
 **/

@Data
public class Test {
    private List<String> lines;

    public static void main(String[] args) {
        Test test = new Test();

        test.setLines(Arrays.asList("上海_大连", "上海_济南"));

        System.out.println(JSONObject.toJSONString(test));
    }
}

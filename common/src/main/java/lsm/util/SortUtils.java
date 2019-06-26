package lsm.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019-01-25 16:55
 **/

public class SortUtils {

    public static void sortByBoolean(List<Boolean> list) {
        list.sort((o1, o2) -> {
            // 两个值进行位运算,有一个true为true
            if (o1 ^ o2) {
                return o1 ? -1 : 1;
            } else {
                return 0;
            }
        });
    }

    public static void sortByValue(List<String> list, String value) {
        list.sort((o1, o2) -> {
            if (Objects.equals(o1, o2)) {
                return 0;
            }
            if (Objects.equals(o1, value)) {
                return -1;
            }
            if (Objects.equals(o2, value)) {
                return 1;
            }
            return 0;

        });
    }



    public static void main(String[] args) {
        List<Boolean> booleans = Arrays.asList(false, true, true, false);
        sortByBoolean(booleans);
        System.out.println(JSONObject.toJSONString(booleans));
        List<String> stringList = Arrays.asList("2", "1", "3", "2", "5", "4", "2", "2", "7", "7", "8");
        sortByValue(stringList, "2");
        System.out.println(JSONObject.toJSONString(stringList));
    }

}

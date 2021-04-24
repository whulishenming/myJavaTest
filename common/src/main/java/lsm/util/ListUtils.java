package lsm.util;

import com.alibaba.fastjson.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lishenming
 * @create 2017-11-09
 **/

public class ListUtils {

    public static <T>  List<T> removeDuplicate(List<T> list){

        return new ArrayList<>(new HashSet<>(list));
    }

    public static <T>  List<T> intersection(List<T> list1, List<T> list2){

        return list1.stream().filter(list2::contains).collect(Collectors.toList());

    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("test1", "testValue     ");


        System.out.println(JSONObject.toJSONString(map));
    }

}

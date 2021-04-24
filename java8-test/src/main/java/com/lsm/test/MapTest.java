package com.lsm.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author lishenming
 * @date 2018/11/1 10:46
 **/
public class MapTest {

    private Map<Integer, String> map1 = new HashMap<>(10);

    @Before
    public void init() {
        for (int i = 0; i < 10; i++) {
            map1.put(i, "val_" + i);
        }
    }

    @Test
    public void testMap() {
        // 1 遍历
        map1.forEach((key, value) -> System.out.println("key=" + key + ",value=" + value));

        // 2 getOrDefault 获取key值,如果key不存在则用defaultValue
        Assert.assertEquals("val_1", map1.getOrDefault(1, "test1"));
        Assert.assertEquals("test11", map1.getOrDefault(11, "test11"));

        // 3 putIfAbsent


        map1.computeIfPresent(3, (num, val) -> val + num * 10);
        map1.computeIfPresent(11, (num, val) -> val + num * 10);

        map1.computeIfAbsent(4, (num) -> "test" + num);
        map1.computeIfAbsent(14, (num) -> "test" + num);

        map1.put(1, null);
        map1.computeIfAbsent(1, num -> "absent");

        System.out.println(map1);

        Map<String, String> map = new HashMap<>(10);

        // JDK8之前的实现方式
        boolean removed = false;

        if (map.containsKey("key") && Objects.equals(map.get("key"), "value")) {
            map.remove("key");
            removed = true;
        }

        // JDK8的实现方式
        boolean removedJdk8 = map.remove("key", "value");

        map.replaceAll((key, value) -> {
            if ("test".equals(key)) {
                return value + "test";
            }
            return value;
        });

        map.compute("test", (key, value) -> {
            if ("test".equals(key)) {
                return value + "test";
            }
            return value;
        });

        map.put("test2", "test2");
        map.put("test4", "test4Value");

        map.merge("test2", "new Value", (oldValue, value) -> oldValue + "_" + value);

        map.computeIfPresent("test4", (key, oldValue) -> key + "_" + oldValue);

        System.out.println(map1);

    }

    @Test
    public void testCompute() {

        Map<String, String> map = new HashMap<>(2);
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        map.put("key4", "value4");

        String value1 = map.compute("key1", (key, oldValue) -> "new Value1");
        String value3 = map.compute("key11", (key, oldValue) -> "new Value3");

        String key2 = map.computeIfAbsent("key2", (key) -> key + "test");
        String key21 = map.computeIfAbsent("key21", (key) -> key + "test");

        String key3 = map.computeIfPresent("key3", (key, oldValue) -> key + "_" + oldValue);
        String key31 = map.computeIfPresent("key31", (key, oldValue) -> key + "_" + oldValue);

        String key4 = map.merge("key4", "new Value4", (oldValue, newValue) -> oldValue + "_" + newValue);
        String key41 = map.merge("key41", "new Value41", (oldValue, newValue) -> oldValue + "_" + newValue);

        System.out.println(map);

    }
}

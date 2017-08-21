package com.lsm.testJava8;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LambdaTest {

    @Test
    public void testListSort() {
        List<Integer> list = Arrays.asList(10, 12, 5, 18, 6, 8, 13);

        list.sort((Integer::compareTo));

        System.out.println(JSONObject.toJSONString(list));

//        list.sort((t1, t2) -> t2 - t1);
        list.sort((Integer t1, Integer t2) -> t2 - t1);

        System.out.println(JSONObject.toJSONString(list));

    }

    @Test  // 比较器链
    public void testComparator() {
        List<String> list = Arrays.asList("tsratsdf", "dsdsd", "dsdsdgahdea", "safrr", "dasaw", "ase");

        list.sort(
                Comparator.comparing(String::length)
                .reversed() // 逆序
                .thenComparing(String::compareTo) // 相同的时候的比较方法
        );

        System.out.println(JSONObject.toJSONString(list));
    }

    @Test  // boolean test(T t);  布尔表达式
    public void testPredicate() {

        List<Integer> results = filter(Arrays.asList(10, 12, 5, 18, 6, 8, 13), (Integer i) -> i.compareTo(10) > 0);

        System.out.println(JSONObject.toJSONString(results));

    }

    @Test  // void accept(T t); 消费一个对象
    public void testConsumer() {
        forEach(Arrays.asList(10, 12, 5, 18, 6, 8, 13), (Integer i) -> System.out.println(i));
    }

    @Test  // T get(); 创建对象
    public void testSupplier() {
        List<Integer> results = getList(10, () -> new Random().nextInt(100));

        System.out.println(JSONObject.toJSONString(results));
    }

    @Test  // R apply(T t); 从一个对象中选择/提取
    public void testFunction() {
        List<Integer> results = deal(Arrays.asList("tsratsdf", "dsdsd", "dsdsdgahdea"), String::length);

        System.out.println(JSONObject.toJSONString(results));

    }

    private <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> results = new ArrayList<>();
        for(T s: list){
            if(p.test(s)){
                results.add(s);
            }
        }
        return results;
    }

    private <T> void forEach(List<T> list, Consumer<T> c){
        for(T i: list){
            c.accept(i);
        }
    }

    private <T> List<T> getList(int length, Supplier<T> s) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(s.get());
        }
        return list;
    }

    private <T, R> List<R> deal(List<T> list, Function<T, R> f) {
        List<R> result = new ArrayList<>();
        for(T s: list){
            result.add(f.apply(s));
        }
        return result;
    }
}

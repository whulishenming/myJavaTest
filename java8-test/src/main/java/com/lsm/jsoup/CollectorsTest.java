package com.lsm.jsoup;

import com.alibaba.fastjson.JSONObject;
import com.lsm.jsoup.domain.Dish;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

public class CollectorsTest {

    private List<Dish> menu = Arrays.asList(
            new Dish("pork", false, 800, Dish.Type.MEAT),
            new Dish("beef", false, 700, Dish.Type.MEAT),
            new Dish("chicken", false, 400, Dish.Type.MEAT),
            new Dish("french fries", true, 530, Dish.Type.OTHER),
            new Dish("rice", true, 350, Dish.Type.OTHER),
            new Dish("season fruit", true, 120, Dish.Type.OTHER),
            new Dish("pizza", true, 550, Dish.Type.OTHER),
            new Dish("prawns", false, 300, Dish.Type.FISH),
            new Dish("salmon", false, 450, Dish.Type.FISH) );

    @Test  //  把流中所有项目收集到一个List
    public void testToList() {
        List<Dish> list= menu.stream().collect(Collectors.toList());

        System.out.println(list);
    }

     @Test  //  把流中所有项目收集到一个Set，删除重复项
    public void testToSet() {
        Set<Dish> set= menu.stream().collect(Collectors.toSet());

        System.out.println(set);
    }

    @Test
    public void testToMap() {
        Map<String, Dish> map = menu.stream().collect(Collectors.toMap(Dish::getName, dish -> dish));
        System.out.println(map);
    }

    @Test  //  计算流中元素的个数
    public void testCounting() {
        long howManyDishes = menu.stream().collect(Collectors.counting());

        System.out.println(howManyDishes);
    }

    @Test  //  对流中项目的一个整数属性求和
    public void testSummingInt() {
        long howManyDishes = menu.stream().collect(Collectors.summingInt(Dish::getCalories));

        System.out.println(howManyDishes);
    }

    @Test  //  计算流中项目Integer 属性的平均值
    public void testAveragingInt() {
        Double average = menu.stream().collect(Collectors.averagingInt(Dish::getCalories));

        System.out.println(average);
    }

    @Test  // 收集关于流中项目Integer 属性的统计值，例如最大、最小、总和与平均值
    public void testSummarizingInt() {
        IntSummaryStatistics summaryStatistics = menu.stream()
                .collect(Collectors.summarizingInt(Dish::getCalories));

        System.out.println(JSONObject.toJSONString(summaryStatistics));
    }

    @Test  //  连接对流中每个项目调用toString 方法所生成的字符串
    public void testJoining() {
        String shortMenu = menu.stream().map(Dish::getName).collect(Collectors.joining(", "));

        System.out.println(shortMenu);
    }

    @Test  //  一个包裹了流中按照给定比较器选出的最大元素的Optional，或如果流为空则为Optional.empty()
    public void testMaxBy() {
        menu.stream()
                .collect(Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)))
                .ifPresent(System.out::println);

    }

    @Test  //  从一个作为累加器的初始值开始，利用BinaryOperator 与流中的元素逐个结合，从而将流归约为单个值
    public void testReducing() {
        int totalCalories = menu.stream().collect(Collectors.reducing(0, Dish::getCalories, (i, j) -> i + j));

        System.out.println(totalCalories);
    }

    @Test  //  包裹另一个收集器，对其结果应用转换函
    public void testCollectingAndThen() {
        Integer size = menu.stream().collect(Collectors.collectingAndThen(Collectors.toList(), List::size));

        System.out.println(size);
    }

    @Test  //  根据项目的一个属性的值对流中的项目作问组，并将属性值作为结果Map 的键
    public void testGroupingBy() {
        Map<Dish.Type,List<Dish>> dishesByType = menu.stream().collect(Collectors.groupingBy(Dish::getType));

        System.out.println(JSONObject.toJSONString(dishesByType));
    }

}

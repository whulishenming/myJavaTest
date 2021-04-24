package com.lsm.jsoup;

import com.alibaba.fastjson.JSONObject;
import com.lsm.jsoup.domain.Dish;
import org.junit.Test;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 流只能遍历一次。遍历完之后，我们就说这个流已经被消费掉了
 * 1.构建流
 *      Stream.of("Java 8 ", "Lambdas ", "In ", "Action");
 *      Arrays.stream({2, 3, 5, 7, 11,13});
 *      Stream.iterate(0, n -> n + 2)
 *      Stream.generate(Math::random)
 *  2. 一个中间操作链，形成一条流的流水线
 *      Stream<T> filter(Predicate<?super T> predicate);
 *      Stream<T> sorted(Comparator<? super T> comparator);
 *      <R> Stream<R> map(Function<? super T, ? extends R> mapper);
 *      Stream<T> limit(long maxSize);
 *      Stream<T> distinct();
 *  3. 一个终端操作，执行流水线，并能生成结果
 *      void forEach(Consumer<? super T> action);
 *      Lambda long count();
 *      <R, A> R collect(Collector<? super T, A, R> collector);
 *      把流归约成一个集合，比如List、Map 甚至是Integer
 *      Optional<T> findFirst();
 *      T reduce(T identity, BinaryOperator<T> accumulator);
 */
public class StreamAPITest {

    private List<Dish> menuList = Arrays.asList(new Dish("pork", false, 800, Dish.Type.MEAT),
        new Dish("beef", false, 700, Dish.Type.MEAT), new Dish("chicken", false, 400, Dish.Type.MEAT),
        new Dish("french fries", true, 530, Dish.Type.OTHER), new Dish("rice", true, 350, Dish.Type.OTHER),
        new Dish("season fruit", true, 120, Dish.Type.OTHER), new Dish("pizza", true, 550, Dish.Type.OTHER),
        new Dish("prawns", false, 300, Dish.Type.FISH), new Dish("salmon", false, 450, Dish.Type.FISH));

    @Test
    public void testFilter() {
        // 用谓词筛选
        List<Dish> vegetarianMenu = menuList.stream().filter(Dish::isVegetarian).collect(Collectors.toList());

        System.out.println(JSONObject.toJSONString(vegetarianMenu));
    }

    @Test
    public void testDistinct() {
        // 筛选各异的元素(（根据流所生成元素的 hashCode 和 equals 方法实现)
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        numbers.stream().distinct().forEach(System.out::println);
    }

    @Test
    public void testLimit() {
        // 截短流
        List<Dish> dishes = menuList.stream().limit(3).collect(Collectors.toList());

        System.out.println(JSONObject.toJSONString(dishes));
    }

    @Test
    public void testSkip() {
        // 跳过元素
        List<Dish> dishes = menuList.stream().skip(2).collect(Collectors.toList());

        System.out.println(JSONObject.toJSONString(dishes));
    }

    @Test
    public void testArrayStream() {
        String[] arrayOfWords = {"Goodbye", "World"};
        Stream<String> stream = Arrays.stream(arrayOfWords);
        stream.forEach(System.out::println);

        List<String> wordList = Arrays.asList("Goodbye", "World");
        Stream<String> listStream = wordList.stream();
        listStream.forEach(System.out::println);
    }

    @Test
    public void testFlatMap() {
        // flatMap方法让你把一个流中的每个值都换成另一个流，然后把所有的流连接 起来成为一个流。
        List<String> words = Arrays.asList("Goodbye", "World");
        List<String> characters = words.stream().flatMap(w -> Arrays.asList(w.split("")).stream())
                .collect(Collectors.toList());
        List<String> uniqueCharacters =
            words.stream().map(w -> w.split("")).flatMap(Arrays::stream).distinct().collect(Collectors.toList());

        System.out.println(JSONObject.toJSONString(uniqueCharacters));
    }

    /**
     * 查找和匹配
     */
    @Test
    public void testAnyMatch() {
        // 流中是否有一个元素能匹配给定的谓词
        boolean hasVegetable = menuList.stream().anyMatch(Dish::isVegetarian);

        System.out.println("hasVegetable: " + hasVegetable);
    }

    @Test
    public void testAllMatch() {
        // 流中的元素是否都能匹配给定的谓词
        boolean isHealthy = menuList.stream().allMatch(d -> d.getCalories() < 1000);

        System.out.println("isHealthy: " + isHealthy);
    }

    @Test
    public void testNoneMatch() {
        // 流中没有任何元素与给定的谓词匹配
        boolean isHealthy = menuList.stream().noneMatch(d -> d.getCalories() >= 1000);

        System.out.println("isHealthy: " + isHealthy);
    }

    @Test
    public void testFindAny() {
        // 将返回当前流中的任意元素
        menuList.stream().filter(Dish::isVegetarian).findAny().ifPresent(d -> System.out.println(d.getName()));

    }

    @Test
    public void testFindFirst() {
        // 将返回当前流中的第一个元素
        Stream.of(1, 2, 3, 4, 5).map(x -> x * x).filter(x -> x % 3 == 0).findFirst().ifPresent(System.out::println);

    }

    /**
     * 归约
     */
    @Test
    public void testReduceToSum() {
        Optional<Integer> reduce = menuList.stream().map(Dish::getCalories).reduce(Integer::sum);

        System.out.println(reduce.get());
    }

    @Test
    public void testReduceToMax() {
        // 返回一个Optional
        menuList.stream().map(Dish::getCalories).reduce(Math::max).ifPresent(System.out::println);
    }

    @Test
    public void testCollect() {
        List<String> list = menuList.stream() // 建立操作流水线
            .filter((d) -> d.getCalories() > 300) // 过滤筛选条件
            .sorted(Comparator.comparing(Dish::getCalories)) // 定义排序方式
            .map(Dish::getName) // 提取出特定字段
            .limit(3) // 截断
            .collect(Collectors.toList()); // 转换成list

        System.out.println(JSONObject.toJSONString(list));
    }

    @Test
    public void testForEach() {
        menuList.stream().filter((d) -> d.getCalories() > 300).sorted(Comparator.comparing(Dish::getCalories))
            .map(Dish::getName).limit(3).forEach(System.out::println);

    }

    @Test
    public void testCount() {
        long count = menuList.stream().filter((d) -> d.getCalories() > 300).count();
        System.out.println(count);
    }

    @Test
    public void testIntStream() {
        // 映射到数值流
        IntStream intStream = menuList.stream().mapToInt(Dish::getCalories);
        // 直接求和 还支持 max、min、average 等等,
        int sum = intStream.sum();
        System.out.println(sum);
        // 转换回对象流, 流只能遍历一次
        Stream<Integer> integerStream = menuList.stream().mapToInt(Dish::getCalories).boxed();
        Integer sum2 = integerStream.reduce(0, Integer::sum);
        System.out.println(sum2);
    }

    @Test
    public void testIterate() {
        // 依次生成一系列值
        Stream.iterate(0, n -> n + 2).limit(10).forEach(System.out::println);
    }

    @Test
    public void testGenerate() {
        // 接受一个Supplier<T>类型的Lambda提供新的值
        Stream.generate(Math::random).limit(5).forEach(System.out::println);
    }

    @Test
    public void testSorted(){
        // 多字段排序
        List<Dish> sortedList = menuList.stream()
                .sorted(Comparator.comparingInt(Dish::getCalories).thenComparing(Comparator.comparing(Dish::getName)))
                .collect(Collectors.toList());

        System.out.println(JSONObject.toJSONString(sortedList));
    }

    @Test
    public void testToList() {
        // 映射
        List<String> nameList = menuList.stream().map(Dish::getName).collect(Collectors.toList());

        System.out.println(JSONObject.toJSONString(nameList));
    }

    @Test
    public void testToSet() {
        // 映射
        Set<String> nameSet = menuList.stream().map(Dish::getName).collect(Collectors.toSet());

        System.out.println(JSONObject.toJSONString(nameSet));
    }

    @Test
    public void testToMap() {
        // name不能重复
        Map<String, Dish> map1 = menuList.stream().collect(Collectors.toMap(Dish::getName, dish -> dish));
        System.out.println(map1);

        // 分组
        Map<Dish.Type, List<Dish>> map2 = menuList.stream().collect(Collectors.groupingBy(Dish::getType));
        System.out.println(map2);
    }

}

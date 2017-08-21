package com.lsm.testJava8;

import com.alibaba.fastjson.JSONObject;
import com.lsm.testJava8.domain.Dish;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 流只能遍历一次。遍历完之后，我们就说这个流已经被消费掉了
 * 1.构建流
 *      Stream.of("Java 8 ", "Lambdas ", "In ", "Action");
 *      Arrays.stream({2, 3, 5, 7, 11, 13});
 *      Stream.iterate(0, n -> n + 2)
 *      Stream.generate(Math::random)
 * 2. 一个中间操作链，形成一条流的流水线
 *      Stream<T> filter(Predicate<? super T> predicate);
 *      Stream<T> sorted(Comparator<? super T> comparator);
 *      <R> Stream<R> map(Function<? super T, ? extends R> mapper);
 *      Stream<T> limit(long maxSize);
 *      Stream<T> distinct();
 * 3. 一个终端操作，执行流水线，并能生成结果
 *      void forEach(Consumer<? super T> action); 消费流中的每个元素并对其应用Lambda
 *      long count(); 返回流中元素的个数
 *      <R, A> R collect(Collector<? super T, A, R> collector);  把流归约成一个集合，比如List、Map 甚至是Integer
 *      Optional<T> findFirst();
 *      T reduce(T identity, BinaryOperator<T> accumulator);
 */
public class StreamAPITest {

    private List<Dish> menuList = Arrays.asList(
            new Dish("pork", false, 800, Dish.Type.MEAT),
            new Dish("beef", false, 700, Dish.Type.MEAT),
            new Dish("chicken", false, 400, Dish.Type.MEAT),
            new Dish("french fries", true, 530, Dish.Type.OTHER),
            new Dish("rice", true, 350, Dish.Type.OTHER),
            new Dish("season fruit", true, 120, Dish.Type.OTHER),
            new Dish("pizza", true, 550, Dish.Type.OTHER),
            new Dish("prawns", false, 300, Dish.Type.FISH),
            new Dish("salmon", false, 450, Dish.Type.FISH) );


    @Test  // 用谓词筛选
    public void testFilter() {
        List<Dish> vegetarianMenu = menuList.stream()
                .filter(Dish::isVegetarian)
                .collect(Collectors.toList());

        System.out.println(JSONObject.toJSONString(vegetarianMenu));
    }

    @Test  // 筛选各异的元素(（根据流所生成元素的 hashCode 和 equals 方法实现)
    public void testDistinct() {
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        numbers.stream()
                .distinct()
                .forEach(System.out::println);
    }

    @Test  // 截短流
    public void testLimit() {
        List<Dish> dishes = menuList.stream()
                .limit(3)
                .collect(Collectors.toList());

        System.out.println(JSONObject.toJSONString(dishes));
    }

    @Test  // 跳过元素
    public void testSkip() {
        List<Dish> dishes = menuList.stream()
                .skip(2)
                .collect(Collectors.toList());

        System.out.println(JSONObject.toJSONString(dishes));
    }

    @Test  // 映射
    public void testMap() {
        List<Integer> dishNameLengths = menuList.stream()
                .map(Dish::getName)     // 返回 Stream<String>
                .map(String::length)    // 返回 Stream<Integer>
                .collect(Collectors.toList());

        System.out.println(JSONObject.toJSONString(dishNameLengths));
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

    @Test // flatmap方法让你把一个流中的每个值都换成另一个流，然后把所有的流连接 起来成为一个流。
    public void testFlatmap() {
        List<String> words = Arrays.asList("Goodbye", "World");
        List<String> uniqueCharacters =
                words.stream()
                        .map(w -> w.split(""))
                        .flatMap(Arrays::stream)
                        .distinct()
                        .collect(Collectors.toList());

        System.out.println(JSONObject.toJSONString(uniqueCharacters));
    }

    /**
     * 查找和匹配
     */
    @Test  // 流中是否有一个元素能匹配给定的谓词
    public void testAnyMatch() {
        boolean hasVegetable = menuList.stream().anyMatch(Dish::isVegetarian);

        System.out.println("hasVegetable: " + hasVegetable);
    }

    @Test  // 流中的元素是否都能匹配给定的谓词
    public void testAllMatch(){
        boolean isHealthy = menuList.stream().allMatch(d -> d.getCalories() < 1000);

        System.out.println("isHealthy: " + isHealthy);
    }
    
    @Test  // 流中没有任何元素与给定的谓词匹配
    public void testNoneMatch() {
        boolean isHealthy = menuList.stream().noneMatch(d -> d.getCalories() >= 1000);

        System.out.println("isHealthy: " + isHealthy);
    }

    @Test  // 将返回当前流中的任意元素
    public void testFindAny() {
        menuList.stream()
                .filter(Dish::isVegetarian)
                .findAny()
                .ifPresent(d -> System.out.println(d.getName()));

    }

    @Test  // 将返回当前流中的第一个元素
    public void testFindFirst() {
        Stream.of(1, 2, 3, 4, 5)
                .map(x -> x * x)
                .filter(x -> x % 3 == 0)
                .findFirst()
                .ifPresent(System.out::println);

    }

    /**
     * 归约
     */
    @Test
    public void testReduceToSum() {
        Integer totalCalories = menuList.stream()
                .map(Dish::getCalories)
//                .reduce(0, Integer::sum);
                .reduce(0, (a, b) -> a + b);

        System.out.println(totalCalories);
    }

    @Test
    public void testReduceToMax() {
        menuList.stream()
                .map(Dish::getCalories)
                .reduce(Math::max)  // 返回一个Optional
                .ifPresent(System.out::println);
    }

    @Test
    public void testCollect() {
        List<String> list = menuList
                .stream()                                               // 建立操作流水线
                .filter((d) -> d.getCalories() > 300)                   // 过滤筛选条件
                .sorted(Comparator.comparing(Dish::getCalories))     // 定义排序方式
                .map(Dish::getName)                                     // 提取出特定字段
                .limit(3)                                               // 截断
                .collect(Collectors.toList());                                     // 转换成list

        System.out.println(JSONObject.toJSONString(list));
    }

    @Test
    public void testForEach() {
        menuList.stream()
                .filter((d) -> d.getCalories() > 300)
                .sorted(Comparator.comparing(Dish::getCalories))
                .map(Dish::getName)
                .limit(3)
                .forEach(System.out::println);

    }

    @Test
    public void testCount() {
        long count = menuList.stream()
                .filter((d) -> d.getCalories() > 300)
                .count();
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

    @Test  // 依次生成一系列值
    public void testIterate() {
        Stream.iterate(0, n -> n + 2)
                .limit(10)
                .forEach(System.out::println);
    }

    @Test  // 接受一个Supplier<T>类型的Lambda提供新的值
    public void testGenerate() {
        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);
    }

}

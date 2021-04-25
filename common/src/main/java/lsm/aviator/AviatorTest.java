package lsm.aviator;

import java.time.LocalDate;
import java.util.*;

import lsm.util.DateUtils;
import lsm.util.EmptyUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.aviator.AviatorEvaluator;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019/10/10 09:01
 **/
public class AviatorTest {
    private Map<String, Object> env = new HashMap<>(10);

    @Before
    public void init() {
        env.put("yourName", "lsm");
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");

        int[] array = new int[3];
        array[0] = 0;
        array[1] = 1;
        array[2] = 3;

        Map<String, String> map = new HashMap<>();
        map.put("year", "2019");

        env.put("list", list);
        env.put("array", array);
        env.put("mmap", map);

        env.put("a", 10);
        env.put("email", "killme2008@gmail.com");

        DomainDemo foo = new DomainDemo(100, 3.14f, new Date(),
                Arrays.asList(LocalDate.parse("2019-10-10"), LocalDate.parse("2018-01-01"), LocalDate.parse("2017-01-01")));
        env.put("foo", foo);

        Map<String,Object> content = new HashMap<>(2);
        content.put("name","emus");
        content.put("age",12);
        List<Map<String,Object>> list1 = new ArrayList<>(1);
        list1.add(content);
        env.put("content",list1);
    }

    @Test
    public void test() {

        Object execute = AviatorEvaluator.execute("a1==nil", new HashMap<>());
        // 执行表达式
        Assert.assertEquals(20L, AviatorEvaluator.execute("10 + 2 * 5"));

        // 多行表达式 允许通过分号 ; 来求值多个表达式，多个表达式的求值结果是最后一个表达式的值：
        Assert.assertEquals(20.0, AviatorEvaluator.execute("a=3; b=2; c=a+b; c*4.0"));

        // 使用变量
        Assert.assertEquals("hello lsm", AviatorEvaluator.execute(" 'hello ' + yourName ", env));

        // 调用函数
        Assert.assertEquals(5L, AviatorEvaluator.execute("string.length('hello')"));
        Assert.assertEquals(true,
            AviatorEvaluator.execute("string.contains(\"test\", string.substring('hello', 1, 2))"));

        // 数组和map
        Assert.assertEquals("hello world", AviatorEvaluator.execute("list[0]+ ' ' +list[1]", env));
        Assert.assertEquals(4L, AviatorEvaluator.execute("array[0]+array[1]+array[2]", env));
        Assert.assertEquals("2019", AviatorEvaluator.execute("mmap.year ", env));

        // 三元操作符
        Assert.assertEquals("yes", AviatorEvaluator.execute("a>0? 'yes':'no'", env));

        // 正则表达式匹配,通过=~操作符,
        // 匹配成功后, Aviator 会自动将匹配成功的捕获分组(capturing groups) 放入 env ${num}的变量中,其中$0 指代整个匹配的字符串,而$1表示第一个分组，$2表示第二个分组以此类推。
        Assert.assertEquals("killme2008",
            AviatorEvaluator.execute("email=~/([\\w0-8]+)@\\w+[\\.\\w+]+/ ? $1 : 'unknow' ", env));

        // 变量的语法糖
        Assert.assertEquals(100, AviatorEvaluator.execute("#foo.i", env));
        Assert.assertEquals(3.14f, AviatorEvaluator.execute("foo.f", env));
        Assert.assertEquals(2019L, AviatorEvaluator.execute("#foo.date.year + 1900", env));

        // 对于深度嵌套并且同时有数组的变量访问， aviator 通过引用变量来支持（quote variable)：引用变量要求以 # 符号开始
        Assert.assertEquals(2018, AviatorEvaluator.execute("#foo.years[1].year", env));

        // nil 对象
        // nil是 Aviator 内置的常量,类似 java 中的null,表示空的值。
        // nil跟null不同的在于,在 java 中null只能使用在==、!=的比较运算符,而nil还可以使用>、>=、<、<=等比较运算符。
        // Aviator 规定,任何对象都比nil大除了nil本身。用户传入的变量如果为null,将自动以nil替代。

        Assert.assertEquals(true, AviatorEvaluator.execute("nil == nil"));
        Assert.assertEquals(true, AviatorEvaluator.execute(" 3> nil"));
        Assert.assertEquals(true, AviatorEvaluator.execute(" true!= nil"));
        Assert.assertEquals(true, AviatorEvaluator.execute(" ' '>nil "));
        Assert.assertEquals(true, AviatorEvaluator.execute(" a==nil "));

        System.out.println("");
    }

    /**
     * Aviator的String是任何用单引号或者双引号括起来的字符序列 String可以比较大小(基于unicode顺序), 可以参与正则匹配, 可以与任何对象相加, 任何对象与String相加结果为String
     * String中也可以有转义字符,如\n、\\、\' 等
     */
    @Test
    public void testString() {
        Assert.assertEquals("hello lsm", AviatorEvaluator.execute(" 'hello ' + yourName ", env));

        System.out.println(AviatorEvaluator.execute("#content", env));
        System.out.println(AviatorEvaluator.execute("#content[0]", env));
        System.out.println(AviatorEvaluator.execute("#content[1]", env));
        System.out.println(AviatorEvaluator.execute("#content.[0].age", env));

        Assert.assertEquals(true, AviatorEvaluator.execute("#content.[0].age>2", env));

    }

    /**
     * 注册函数通过AviatorEvaluator.addFunction方法, 移除可以通过removeFunction
     */
    @Test
    public void testFunction() {
        // 注册函数
        AviatorEvaluator.addFunction(new AddFunction());
        AviatorEvaluator.addFunction(new AddAllFunction());

        Assert.assertEquals(3d, AviatorEvaluator.execute("add(1, 2)"));
        Assert.assertEquals(103d, AviatorEvaluator.execute("add(add(1, 2), 100)"));
        Assert.assertEquals(10d, AviatorEvaluator.execute("addAll(1, 2, 3, 4)"));
    }

    /**
     * 导入静态方法
     */
    @Test
    public void testStaticFunctions() throws Exception {
        AviatorEvaluator.addStaticFunctions("DateUtils", DateUtils.class);

        Assert.assertEquals(2019L,
            AviatorEvaluator.execute("DateUtils.getYear('2019-10-10 10:01:01', 'yyyy-MM-dd HH:mm:ss')"));

        AviatorEvaluator.addStaticFunctions("EmptyUtils", EmptyUtils.class);

        List<String> testEmpty = new ArrayList<>();
        testEmpty.add("1");
        env.put("testEmpty", testEmpty);
        Assert.assertEquals(false, AviatorEvaluator.execute("EmptyUtils.isEmpty(testEmpty)", env));
    }

    /**
     * 导入实例方法 第一个参数传入实例
     */
    @Test
    public void testInstanceFunctions() throws Exception {
        AviatorEvaluator.addInstanceFunctions("str", String.class);

        Assert.assertEquals(10L, AviatorEvaluator.execute("str.length('2019-10-10')"));
    }

    /**
     * 同时导入静态方法和实例方法
     */
    @Test
    public void testImportFunctions() throws Exception {
        AviatorEvaluator.importFunctions(TestFunction.class);

        Assert.assertEquals(3L, AviatorEvaluator.execute("TestFunction.add(1, 2)"));
    }

}

package lsm.bigDecimal;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by za-lishenming on 2017/5/9.
 */
public class BigDecimalTest {


    /**
     * 1. 测试构造方法
     * 2. 用String构造不会丢失精度
     */
    @Test
    public void testConstructor() {
        BigDecimal aDouble =new BigDecimal(1.22);
        System.out.println("construct with a double value: " + aDouble);
        BigDecimal aString = new BigDecimal("1.22");
        System.out.println("construct with a String value: " + aString);
        System.out.println(BigDecimal.ZERO);
    }

    /**
     * 1. 加减乘除测试
     */
    @Test
    public void testOperation() {
        BigDecimal a = new BigDecimal("10.5");
        BigDecimal b = new BigDecimal("0.5");
        System.out.println("a + b = " + a.add(b));
        System.out.println("a - b = " + a.subtract(b));
        System.out.println("a * b = " + a.multiply(b));
        System.out.println("a / b = " + a.divide(b));
    }

    /**
     * 1. 进度处理的测试
     * 2. setScale方法中，第一个参数是设置保留小数位数，第二个参数是这是舍入方式
     */
    @Test
    public void testScale() {
        BigDecimal num =new BigDecimal(12.345623);
        //四舍五入
        System.out.println(num.setScale(2, BigDecimal.ROUND_HALF_UP));
        //舍入趋向于零的方式
        System.out.println(num.setScale(2, BigDecimal.ROUND_DOWN));
        //舍入远离零的方式
        System.out.println(num.setScale(2, BigDecimal.ROUND_UP));
    }
}

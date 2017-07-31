package lsm.integer;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by za-lishenming on 2017/5/10.
 */
public class IntegerTest {

    /**
     * String 转 Integer 测试
     */
    @Test
    public void testIntegerToString(){
        System.out.println(Integer.parseInt("100"));
        System.out.println(Integer.valueOf("100"));
        //假设String参数是一个系统属性数值的名称，会读取该系统属性，然后把系统属性的值转换成一个数字
        System.out.println(Integer.getInteger("100"));

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        /*cal.add(Calendar.HOUR, 24);*/
        Date nextDate = cal.getTime();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nextDate));

    }

}

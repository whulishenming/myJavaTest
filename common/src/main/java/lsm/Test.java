package lsm;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019/8/26 11:19
 **/

public class Test {

    @org.junit.Test
    public void test() {
        ThreadLocal<Long> threadLocal = new ThreadLocal<>();
        threadLocal.set(11L);
        threadLocal.get();

        String ss = "EVENT_1234";
        System.out.println(ss.substring(6));
    }
}

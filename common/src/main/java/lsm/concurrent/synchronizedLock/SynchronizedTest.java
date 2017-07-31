package lsm.concurrent.synchronizedLock;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lishenming on 2017/5/13.
 * 1. synchronized 锁的是对象，如果不是同一个对象，相当于没有加锁
 */
public class SynchronizedTest {
    private int count = 10;

    public  void test(Object o){
        synchronized(o){
            count--;
            System.out.println(Thread.currentThread().getName() + ":" + count);
        }

    }

    @Test
    public void testSynchronized(){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Object o = new Object();
        for (int i = 0; i < 10; i++) {
            //有效果
            executorService.execute( () ->test(o));
            //无效果
            executorService.execute( () ->test(new Object()));
        }

    }

}

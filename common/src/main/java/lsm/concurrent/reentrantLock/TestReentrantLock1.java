package lsm.concurrent.reentrantLock;

import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lishenming on 2017/3/2.
 * 1. 类似于synchronized功能
 * 2. 实现可轮询的锁请求  lock.tryLock()
 * 3. 实现可定时的锁请求  tryLock(long, TimeUnit)
 * 4. 实现可中断的锁获取请求 lockInterruptibly()
 * 5. lock 必须在 finally 块中释放
 */
public class TestReentrantLock1 {

    @Test
    public  void testLock1() {
        final Lock queueLock = new ReentrantLock();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    queueLock.lock();
                    System.out.println(Thread.currentThread().getName() + ": start");
                }finally{
                    queueLock.unlock();
                }
                /*synchronized (this){
                    System.out.println(Thread.currentThread().getName() + ": start");
                }*/
            }).start();
        }
    }

}

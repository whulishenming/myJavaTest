
package lsm.concurrent.countDownLatch;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.*;

/**
 * Created by za-lishenming on 2017/5/14.
 * 某个线程需要等待一个或多个线程操作结束（或达到某种状态）才开始执行
 * 1. countDown() 如果当前计数器的值大于1，则将其减1；若当前值为1，则将其置为0并唤醒所有通过await等待的线程；若当前值为0，则什么也不做直接返回
 * 2. await() 等待计数器的值为0，若计数器的值为0则该方法返回;若等待期间该线程被中断，则抛出InterruptedException并清除该线程的中断状态。
 * 3. await(long timeout, TimeUnit unit) 在指定的时间内等待计数器的值为0，若在指定时间内计数器的值变为0，则该方法返回true；若指定时间内计数器的值仍未变为0，则返回false；
 *                                       若指定时间内计数器的值变为0之前当前线程被中断，则抛出InterruptedException并清除该线程的中断状态。
 * 4. getCount() 读取当前计数器的值
 */
public class CountDownLatchTest {
    @Test
    public void testCountDownLatch() throws ExecutionException, InterruptedException {
        int totalThread = 3;
        long start = System.currentTimeMillis();
        CountDownLatch countDown = new CountDownLatch(totalThread);
        for(int i = 0; i < totalThread; i++) {
            final String threadName = "Thread " + i;
            new Thread(() -> {
                System.out.println(String.format("%s\t%s %s", new Date(), threadName, "started"));
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                countDown.countDown();
                System.out.println(String.format("%s\t%s %s", new Date(), threadName, "ended"));
            }).start();
        }
        countDown.await();
        long stop = System.currentTimeMillis();
        System.out.println(String.format("Total time : %sms", (stop - start)));

    }
}

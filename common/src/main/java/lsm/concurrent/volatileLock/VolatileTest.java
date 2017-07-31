package lsm.concurrent.volatileLock;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by lishenming on 2017/5/13.
 * volatile只能保证线程的可见性，不能保证原子性
 */
public class VolatileTest {

    private volatile boolean running = true;

    private volatile int count = 1;

   /* private boolean running = true;*/

    public void run(){
        System.out.println(Thread.currentThread().getName() + "start");
        while (running){
            /*System.out.println("***************running*****************");*/
        }
        System.out.println(Thread.currentThread().getName() + "end");
    }

    /**
     * 线程的可见性
     * @throws InterruptedException
     */
    @Test
    public void testVolatile1() throws InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> run());
        TimeUnit.SECONDS.sleep(1);

        running = false;
    }

    /**
     * 不能保证原子性
     */
    @Test
    public void testVolatile2() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 10000; j++) {
                    count++;
                }
            });
        }
        System.out.println(count);
    }

}

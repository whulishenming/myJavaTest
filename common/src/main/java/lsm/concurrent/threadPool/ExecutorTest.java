package lsm.concurrent.threadPool;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by za-lishenming on 2017/5/9.
 */
public class ExecutorTest {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "-Start");
//        ExecutorService executorService = Executors.newFixedThreadPool(5);
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Runnable runnable = (() -> test());
        executorService.execute(runnable);

//        executorService.shutdown();
        System.out.println(Thread.currentThread().getName() + "-End");
    }
    @Test
    public void testExecutors(){
        System.out.println(Thread.currentThread().getName() + "-Start");
//        ExecutorService executorService = threadPool.newFixedThreadPool(5);
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Runnable runnable = (() -> test());
        executorService.execute(runnable);

//        executorService.shutdown();
        System.out.println(Thread.currentThread().getName() + "-End");
    }

    public static void test(){
       /* try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        System.out.println(Thread.currentThread().getName()+"-Start" + 1/0);
    }
}

package lsm.concurrent.threadPool;

import org.junit.Test;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by za-lishenming on 2017/5/9.
 */
public class ExecutorTest {

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 5, 1L, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(), new ThreadFactory() {
                AtomicInteger count = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("submit-asyn".concat(":").concat(String.valueOf(count.incrementAndGet())));
                    thread.setDaemon(true);
                    return thread;
                }
            });

    ThreadPoolExecutor threadPoolExecutor2 = new ThreadPoolExecutor(2, 2, 5, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10), new CustomizableThreadFactory("DelayingQueue"), new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "-Start");
        // ExecutorService executorService = Executors.newFixedThreadPool(5);
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Runnable runnable = (ExecutorTest::test);
        executorService.execute(runnable);

        // executorService.shutdown();
        System.out.println(Thread.currentThread().getName() + "-End");


    }

    @Test
    public void testExecutors() {
        System.out.println(Thread.currentThread().getName() + "-Start");
        // ExecutorService executorService = threadPool.newFixedThreadPool(5);
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Runnable runnable = (() -> test());
        executorService.execute(runnable);

        // executorService.shutdown();
        System.out.println(Thread.currentThread().getName() + "-End");
    }

    @Test
    public void testThreadPoolExecutor() throws ExecutionException, InterruptedException {

        threadPoolExecutor.execute(() -> System.out.println("第1个线程"));
        long start = System.currentTimeMillis();
        Future<Integer> future2 = threadPoolExecutor.submit(() -> test2(10, 5));
        Future<Integer> future3 = threadPoolExecutor.submit(() -> test3(10, 5));
        Future<Integer> future4 = threadPoolExecutor.submit(() -> test4(10, 5));

        Integer integer2 = future2.get();
        Integer integer3 = future3.get();
        Integer integer4 = future4.get();

        System.out.println(integer2);
        System.out.println(integer3);
        System.out.println(integer4);

        System.out.println(System.currentTimeMillis() - start);

    }

    public int test2(int a, int b) {
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        return a + b;
    }

    public int test3(int a, int b) {
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        return a * b;
    }

    public int test4(int a, int b) {
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        return a * a + b * b;
    }

    public static void test() {

        /*
         * try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
         */
        System.out.println(Thread.currentThread().getName() + "-Start" + 1 / 0);
    }
}

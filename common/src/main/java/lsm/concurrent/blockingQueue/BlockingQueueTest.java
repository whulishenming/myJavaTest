package lsm.concurrent.blockingQueue;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * Created by za-lishenming on 2017/5/16.
 */
public class BlockingQueueTest {
    private BlockingQueue<String> bq = new ArrayBlockingQueue<>(10);

    @Test
    public void testBlockingQueue1(){
        new Thread(()->{
            int i = 0;
            while (true){
                try {
                    System.out.println("我生产了一个" + ++i);
                    bq.put(i + "");

//                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(()->{
                while (true){
                    try {
                        System.out.println("我消费了一个" + bq.take());
//                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }).start();

    }

    @Test
    public void test(){
        new Thread(()->{
            for (int i = 0; i < 100; i++) {
                try {
                    System.out.println("iiiii");
                    Thread.sleep(100);
//                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}

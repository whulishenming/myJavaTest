package lsm.delayqueue;

import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.netty.util.HashedWheelTimer;
import org.junit.Test;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/6/30 17:11
 **/

public class DelayQueueTest {

    @Test
    public void testDelayedMessage() {
        DelayQueue<DelayedMessage> delayQueue = new DelayQueue<>();

        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int delayTime = random.nextInt(100000);
            DelayedMessage message = new DelayedMessage(i, String.format("content%s", i), delayTime);
            delayQueue.add(message);
        }

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100, 100, 1L, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(), new CustomizableThreadFactory("DelayedQueue"));

        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.execute(() -> {
                while (true) {
                    try {
                        DelayedMessage message = delayQueue.take();
                        System.out.println("currentTimeMillis=" + System.currentTimeMillis() + ", message="
                            + JSONObject.toJSONString(message));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        try {
            Thread.sleep(100000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHashedWheelTimer() {
        HashedWheelTimer timer = new HashedWheelTimer();
        
        Random random = new Random();
        
        for (int i = 0; i < 100; i++) {
            int delayTime = random.nextInt(100000);
            long now = System.currentTimeMillis();

            HashedWheelTimerMessage message = new HashedWheelTimerMessage(i, String.format("content%s, execute time:%s", i, now
                    + delayTime));
            timer.newTimeout(message, delayTime, TimeUnit.MILLISECONDS);
        }

        try {
            Thread.sleep(100000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

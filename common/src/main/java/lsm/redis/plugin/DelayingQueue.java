package lsm.redis.plugin;

import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import lsm.redis.RedisClient;
import redis.clients.jedis.Jedis;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/1/22 14:19
 * 延时队列
 **/

public class DelayingQueue<T> {

    static class TaskItem<T> {
        public String id;
        public T msg;
    }

//    private Type taskType = new TypeReference<TaskItem<T>>() {}.getType();

    private Jedis jedis;
    private String queueKey;

    public DelayingQueue(Jedis jedis, String queueKey) {
        this.jedis = jedis;
        this.queueKey = queueKey;
    }

    public void delay(T msg) {
        TaskItem<T> task = new TaskItem<>();
        task.id = UUID.randomUUID().toString();
        task.msg = msg;
        String s = JSONObject.toJSONString(task);
        // 塞入延时队列 ,5s 后再试
        jedis.zadd(queueKey, System.currentTimeMillis() + 5000, s);
    }

    public void loop() {
        while (!Thread.interrupted()) {
            // 只取一条
            Set<String> values = jedis.zrangeByScore(queueKey, 0, System.currentTimeMillis(), 0, 1);
            if (values.isEmpty()) {
                try {
                    // 歇会继续
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
                continue;
            }
            String s = values.iterator().next();
            // 抢到了
            if (jedis.zrem(queueKey, s) > 0) {
                TaskItem<T> task = JSONObject.parseObject(s, new TypeReference<TaskItem<T>>() {});
                this.handleMsg(task.msg);
            }
        }
    }

    public void handleMsg(T msg) {
        System.out.println(msg);
    }

    @Test
    public void test(){
        DelayingQueue<String> queue = new DelayingQueue<>(RedisClient.getJedisResource(), "testQueue");

        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                queue.delay("codehole" + i);
            }
        });
        Thread consumer = new Thread(() -> queue.loop());
        producer.start();
        consumer.start();
        try {
            producer.join();
            Thread.sleep(6000);
            consumer.interrupt();
            consumer.join();
        } catch (InterruptedException e) {
        }
    }

}

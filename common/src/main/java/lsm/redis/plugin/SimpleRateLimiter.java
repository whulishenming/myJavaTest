package lsm.redis.plugin;

import lsm.redis.RedisClient;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.io.IOException;
import java.util.UUID;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/1/22 15:40
 * 简单限流,限定用户的某个行为在指定的时间里只能允许发生 N 次(没访问成功的也会算一次请求)
 **/

public class SimpleRateLimiter {
    private Jedis jedis = RedisClient.getJedisResource();

    public boolean isActionAllowed(String userId, String actionKey, int period, int maxCount) {
        String key = String.format("hist:%s:%s", userId, actionKey);
        long currentTimeMillis = System.currentTimeMillis();
        Pipeline pipe = jedis.pipelined();
        // 用了multi，也就是事务，能保证一系列指令的原子顺序执行
        pipe.multi();
        pipe.zadd(key, currentTimeMillis, UUID.randomUUID().toString().replaceAll("-", ""));
        pipe.zremrangeByScore(key, 0, currentTimeMillis - period * 1000);
        Response<Long> count = pipe.zcard(key);
        pipe.expire(key, period + 1);
        pipe.exec();
        try {
            pipe.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count.get() <= maxCount;
    }

    @Test
    public void test() {
        for (int i = 0; i < 20; i++) {
            System.out.println(isActionAllowed("user_lsm", "list", 60, 5));
        }
    }
}

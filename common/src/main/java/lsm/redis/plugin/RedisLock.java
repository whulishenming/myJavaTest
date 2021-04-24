package lsm.redis.plugin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lsm.redis.RedisClient;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/1/22 14:12
 * 分布式锁
 **/
@Slf4j
@AllArgsConstructor
public class RedisLock {
    private Jedis jedis;

    public boolean lock(String key) {
        return jedis.set(key, "", "nx", "ex", 5L) != null;
    }

    public void unlock(String key) {
        jedis.del(key);
    }

    @Test
    public void testLock() {
        RedisLock redisLock = new RedisLock(RedisClient.getJedisResource());
        String lockKey = "RedisLockKey";

        log.info(String.valueOf(redisLock.lock(lockKey)));
        redisLock.unlock(lockKey);
    }
}

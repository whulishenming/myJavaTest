package lsm.redis.plugin;

import lombok.extern.slf4j.Slf4j;
import lsm.redis.RedisClient;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/1/22 10:38
 * 分布式锁--可重入锁
 **/

@Slf4j
public class ReentrantLock {

    private Jedis jedis = RedisClient.getJedisResource();

    private RedisLock redisLock = new RedisLock(jedis);

    private ThreadLocal<Map<String, Integer>> lockers = new ThreadLocal<>();

    private Map<String, Integer> currentLockers() {
        Map<String, Integer> refs = lockers.get();
        if (refs != null) {
            return refs;
        }
        lockers.set(new HashMap<>());
        return lockers.get();
    }

    public boolean lock(String key) {
        Map<String, Integer> refs = currentLockers();
        Integer refCnt = refs.get(key);
        if (refCnt != null) {
            refs.put(key, refCnt + 1);
            return true;
        }
        boolean ok = redisLock.lock(key);
        if (!ok) {
            return false;
        }
        refs.put(key, 1);
        return true;
    }

    public boolean unlock(String key) {
        Map<String, Integer> refs = currentLockers();
        Integer refCnt = refs.get(key);
        if (refCnt == null) {
            return false;
        }
        refCnt -= 1;
        if (refCnt > 0) {
            refs.put(key, refCnt);
        } else {
            refs.remove(key);
            redisLock.unlock(key);
        }
        return true;
    }

    @Test
    public void testReentrantLock() {
        String lockKey = "ReentrantLockKey";

        log.info(String.valueOf(lock(lockKey)));
        log.info(String.valueOf(lock(lockKey)));

        log.info(String.valueOf(unlock(lockKey)));
        log.info(String.valueOf(unlock(lockKey)));
    }
}

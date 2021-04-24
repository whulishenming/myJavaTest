package lsm.redis;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/1/22 09:46
 **/
@Slf4j
public class RedisClient {

    public static JedisPool getJedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(8);
        config.setMaxIdle(8);
        config.setMaxWaitMillis(-1);

        JedisPool pool = null;
        try {
            pool = new JedisPool(config, "127.0.0.1", 6379, 100);
        } catch (Exception e) {
            log.error("can not create JedisPool.", e);
        }
        log.info("init JedisPool ...");
        return pool;
    }

    public static Jedis getJedisResource() {
        return getJedisPool().getResource();
    }
}

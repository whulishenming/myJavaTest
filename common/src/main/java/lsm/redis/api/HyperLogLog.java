package lsm.redis.api;

import lombok.extern.slf4j.Slf4j;
import lsm.redis.RedisClient;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Random;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/1/22 14:57
 * 可以用来计算UV
 **/

@Slf4j
public class HyperLogLog {
    private Jedis jedis = RedisClient.getJedisResource();

    /**
     * 增加计数, elements去重
     */
    public Long pfadd(final String key, final String... elements){
        return jedis.pfadd(key, elements);
    }

    /**
     * 获取计数
     */
    public Long pfcount(final String key){
        return jedis.pfcount(key);
    }

    /**
     * 用于将多个 pf 计数值累加在一起形成一个新的 pf 值
     */
    public String pfmerge(final String destKey, final String... sourceKeys) {
        return jedis.pfmerge(destKey, sourceKeys);
    }

    @Test
    public void test() {
        for (int i = 0; i < 1000; i++) {
            pfadd("testHyperLogLog1", String.valueOf(new Random().nextInt(100)));
            pfadd("testHyperLogLog2", String.valueOf(new Random().nextInt(100)));
        }

        Long count1 = pfcount("testHyperLogLog1");
        Long count2 = pfcount("testHyperLogLog2");

        String pfmerge = pfmerge("testHyperLogLog", "testHyperLogLog1", "testHyperLogLog2");

        Long count = pfcount("testHyperLogLog");

        log.info("count1={}, count2={}, count={}", count1, count2, count);
    }

}

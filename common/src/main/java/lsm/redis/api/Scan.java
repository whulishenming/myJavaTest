package lsm.redis.api;

import java.util.Map;

import lsm.redis.RedisClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/2/3 20:51
 * 
 * keys的缺点
 *      1. 没有 offset、limit 参数，一次性吐出所有满足条件的 key
 *      2. keys 算法是遍历算法，复杂度是 O(n)，如果实例中有千万级以上的 key，这个指令就会导致 Redis 服务卡顿，
 *      所有读写 Redis 的其它的指令都会被延后甚至会超时报错，因为 Redis 是单线程程序，顺序执行所有指令，其它指令必须等到当前的 keys 指令执行完了才可以继续
 * scan的特点
 *      1. 复杂度虽然也是 O(n)，但是它是通过游标分步进行的，不会阻塞线程;
 *      2. 提供 limit 参数，可以控制每次返回结果的最大条数，limit 只是一个 hint，返回的结果可多可少;
 *      3. 同 keys 一样，它也提供模式匹配功能;
 *      4. 服务器不需要为游标保存状态，游标的唯一状态就是 scan 返回给客户端的游标整数;
 *      5. 返回的结果可能会有重复，需要客户端去重复，这点非常重要;
 *      6. 遍历的过程中如果有数据修改，改动后的数据能不能遍历到是不确定的;
 *      7. 单次返回的结果是空的并不意味着遍历结束，而要看返回的游标值是否为零;
 **/

public class Scan {
    private Jedis jedis = RedisClient.getJedisResource();

    public ScanResult<String> scan(final String cursor) {
        return jedis.scan(cursor);
    }

    /**
     * @param cursor 游标值，游标值不为零，意味着遍历还没结束
     * @param params 有两个参数
     *      count：表示返回keys的数量
     *      match：表示匹配正则表达式
     */
    public ScanResult<String> scan(final String cursor, final ScanParams params) {
        return jedis.scan(cursor, params);
    }

    public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor) {
        return jedis.hscan(key, cursor);
    }

    public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor,
            final ScanParams params) {
        return jedis.hscan(key, cursor, params);
    }

    public ScanResult<String> sscan(final String key, final String cursor) {
        return jedis.sscan(key, cursor);
    }

    public ScanResult<String> sscan(final String key, final String cursor, final ScanParams params) {
        return jedis.sscan(key, cursor, params);
    }

    public ScanResult<Tuple> zscan(final String key, final String cursor) {
        return jedis.zscan(key, cursor);
    }

    public ScanResult<Tuple> zscan(final String key, final String cursor, final ScanParams params) {
        return jedis.zscan(key, cursor);
    }


}

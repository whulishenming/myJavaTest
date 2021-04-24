package lsm.redis.api;

import java.util.Map;
import java.util.Set;

import lsm.redis.RedisClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/2/3 17:39
 *
 * 对有序集合的操作
 *  1. Redis中的有序集合类型，实际上是在集合类型上，为每个元素都关联一个分数，有序实际上说的是分数有序，我们根据分数的范围获取集合及其他操作
 *  2. Redis sorted set的内部使用HashMap和跳跃表(SkipList)来保证数据的存储和有序，
 *     HashMap里放的是成员到score的映射，而跳跃表里存放的 是所有的成员，
 *     排序依据是HashMap里存的score
 *  3. 有序集合可以（通过改变分数）调整元素的位置
 *  http://www.cnblogs.com/WJ5888/p/4516782.html11
 **/

public class ZSet {

    private Jedis jedis = RedisClient.getJedisResource();

    /**
     * 增加元素,如果member存在，则score会覆盖原有的分数
     *  1. 返回是否成功
     */
    public Long zAdd(String key, double score, String member){
        return jedis.zadd(key, score, member);
    }

    public Long zadd(final String key, final double score, final String member, final ZAddParams params) {
        return jedis.zadd(key, score, member, params);
    }

    public Long zadd(final String key, final Map<String, Double> scoreMembers){
        return jedis.zadd(key, scoreMembers);
    }

    public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params){
        return jedis.zadd(key, scoreMembers, params);
    }

    /**
     * 用于移除有序集中的一个或多个成员，不存在的成员将被忽略
     */
    public Long zrem(final String key, final String... members){
        return jedis.zrem(key, members);
    }

    /**
     * 移除有序集中，指定排名(rank)区间内的所有成员
     */
    public Long zremrangeByRank(String key, long start, long end) {
        return jedis.zremrangeByRank(key, start, end);
    }

    /**
     * 移除有序集中，指定分数（score）区间内的所有成员
     */
    public Long zremrangeByScore(final String key, final double start, final double end) {
        return jedis.zremrangeByScore(key, start, end);
    }

    /**
     * 返回有序集中，指定区间内的成员
     *  1. 成员的位置按分数值递增(从小到大)来排序
     *  2. start 和 stop 都以 0 为底
     *  3.  -1 表示最后一个成员， -2 表示倒数第二个成员
     */
    public Set<String> zrange(final String key, final long start, final long end){
        return jedis.zrange(key, start, end);
    }

    /**
     * 返回有序集中，指定区间内的成员
     *  1. 成员的位置按分数值递减(从大到小)来排列
     *  2. start 和 stop 都是倒序的序号
     */
    public Set<String> zrevrange(final String key, final long start, final long end){
        return jedis.zrevrange(key, start, end);
    }

    /**
     * 返回有序集中包含分数，其他同 zrange(String key, long start, long end)
     */
    public Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
        return jedis.zrangeWithScores(key, start, end);
    }

    public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long end){
        return jedis.zrevrangeWithScores(key, start, end);
    }

    /**
     * 返回有序集合中指定分数区间的成员列表。有序集成员按分数值递增(从小到大)次序排列
     *  1. 区间的取值使用闭区间
     */
    public Set<String> zrangeByScore(final String key, final double min, final double max) {
        return jedis.zrangeByScore(key, min, max);
    }

    /**
     * offset 表示从第 offset 个开始，查找 count 个
     *  1. offset从0开始
     */
    public Set<String> zrangeByScore(final String key, final double min, final double max, final int offset, final int count){
        return jedis.zrangeByScore(key, min, max, offset, count);
    }


    public Set<String> zrevrangeByScore(final String key, final double max, final double min) {
        return jedis.zrevrangeByScore(key, min, max);
    }

    public Set<String> zrevrangeByScore(final String key, final double min, final double max, final int offset, final int count) {
        return jedis.zrevrangeByScore(key, min, max, offset, count);
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        return jedis.zrangeByScoreWithScores(key, min, max);
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count){
        return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, double min, double max) {
        return jedis.zrevrangeByScoreWithScores(key, min, max);
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count){
        return jedis.zrevrangeByScoreWithScores(key, min, max, offset, count);
    }



    /**
     * 对有序集合中指定成员的分数加上增量 increment
     *  1. 可以是一个负数值 increment ，让分数减去相应的值
     *  2. 当 key 不存在或者 member 不存在时，相当于zAdd(key, member, score)
     *  3. 返回新的分数
     */
    public Double zincrby(final String key, final double score, final String member){
        return jedis.zincrby(key, score, member);
    }

    public Double zincrby(String key, double score, String member, ZIncrByParams params){
        return jedis.zincrby(key, score, member, params);
    }

    /**
     * 返回有序集中指定成员的排名,其中有序集成员按分数值递增(从小到大)顺序排列
     *  1. 排名以 0 为底
     */
    public Long zrank(final String key, final String member){
        return jedis.zrank(key, member);
    }

    /**
     * 返回有序集中成员的排名。其中有序集成员按分数值递减(从大到小)排序
     *  1. 排名以 0 为底
     */
    public Long zrevrank(final String key, final String member){
        return jedis.zrevrank(key, member);
    }





    /**
     * 计算集合中元素的数量
     *  1. 当 key 存在时，返回有序集的基数
     *  2. 当 key 不存在时，返回 0
     */
    public Long zcard(final String key){
        return jedis.zcard(key);
    }

    /**
     * 返回有序集中，成员的分数值
     */
    public Double zscore(final String key, final String member){
        return jedis.zscore(key, member);
    }

    /**
     * 计算有序集合中指定分数区间的成员数量
     * min 和 max 都是闭区间
     */
    public Long zcount(final String key, final double min, final double max){
        return jedis.zcount(key, min, max);
    }

    /**
     * 交集
     */
    public Long zinterstore(final String dstkey, final String... sets){
        return jedis.zinterstore(dstkey, sets);
    }

    /**
     * 交集
     */
    public Long zinterstore(final String dstkey, final ZParams params, final String... sets) {
        return jedis.zinterstore(dstkey, params, sets);
    }

    /**
     * 并集
     */
    public Long zunionstore(String dstkey, String... sets) {
        return jedis.zunionstore(dstkey, sets);
    }

    /**
     * 并集
     */
    public Long zunionstore(final String dstkey, final ZParams params, final String... sets){
        return jedis.zunionstore(dstkey, params, sets);
    }

}

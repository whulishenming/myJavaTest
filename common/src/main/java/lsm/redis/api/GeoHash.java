package lsm.redis.api;

import lsm.redis.RedisClient;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.geo.GeoRadiusParam;

import java.util.List;
import java.util.Map;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/2/3 16:41
 * 
 *  内部结构实际上只是一个 zset(skiplist)
 **/

public class GeoHash {
    private Jedis jedis = RedisClient.getJedisResource();

    /**
     * 增加
     * 
     */
    public Long geoadd(final String key, final double longitude, final double latitude, final String member) {
        return jedis.geoadd(key, longitude, latitude, member);
    }

    /**
     * 增加
     * 
     */
    public Long geoadd(final String key, final Map<String, GeoCoordinate> memberCoordinateMap) {
        return jedis.geoadd(key, memberCoordinateMap);
    }

    /**
     * zset 删除
     * 
     */
    public Long georem(final String key, final String... members) {
        return jedis.zrem(key, members);
    }

    /**
     * 计算两个元素之间的距离
     * 
     */
    public Double geodist(final String key, final String member1, final String member2) {
        return jedis.geodist(key, member1, member2);
    }

    /**
     * 计算两个元素之间的距离
     * 
     * @param unit
     *            M, KM, MI, FT，分别代表米、千米、英里和尺
     */
    public Double geodist(final String key, final String member1, final String member2, final GeoUnit unit) {
        return jedis.geodist(key, member1, member2, unit);
    }

    /**
     * 获取集合中任意元素的经纬度坐标
     * 
     */
    public List<GeoCoordinate> geopos(final String key, final String... members) {
        return jedis.geopos(key, members);
    }

    /**
     * 可以获取元素的经纬度编码字符串
     * 
     */
    public List<String> geohash(final String key, final String... members) {
        return jedis.geohash(key, members);
    }

    /**
     * member 范围 radius unit 以内的元素按距离正排，它不会排除自身
     * 
     * @param key
     * @param member
     * @param radius
     * @param unit M, KM, MI, FT，分别代表米、千米、英里和尺
     * @return
     */
    public List<GeoRadiusResponse> georadiusbymember(String key, String member, double radius, GeoUnit unit) {
        return jedis.georadiusByMember(key, member, radius, unit);
    }

    /**
     * member 范围 radius unit 以内的元素按距离正排，它不会排除自身
     * @param key
     * @param member
     * @param radius
     * @param unit M, KM, MI, FT，分别代表米、千米、英里和尺
     * @param param
     *          WITHCOORD,WITHDIST,ASC,DESC,COUNT
     * @return
     */
    public List<GeoRadiusResponse> georadiusbymember(String key, String member, double radius, GeoUnit unit,
        GeoRadiusParam param) {
        return jedis.georadiusByMember(key, member, radius, unit, param);
    }

    /**
     * 根据坐标值来查询附近的元素
     */
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius,
        GeoUnit unit) {
        return jedis.georadius(key, longitude, latitude, radius, unit);
    }

    /**
     * 根据坐标值来查询附近的元素
     */
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit,
        GeoRadiusParam param) {
        return jedis.georadius(key, longitude, latitude, radius, unit, param);
    }

}

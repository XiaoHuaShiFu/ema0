package com.ema.cache.impl;

import com.ema.cache.IRedisHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Map;

/**
 * 描述:
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-03-12 16:57
 */
@Repository("iRedisHash")
public class RedisHashImpl implements IRedisHash {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 把field添加到缓存里,并添加添加的时间
     * @param key key
     * @param field field名
     * @return 成功修改行数
     */
    public Long set(String key, String field) {
        Jedis jedis = jedisPool.getResource();
        Long time = new Date().getTime();
        Long rowCount = jedis.hset(key, field, time.toString());
        jedis.close();
        return rowCount;
    }

    /**
     * 获取对应key的对应field的值
     *
     * @param key key
     * @param field field名
     * @return 获取到的字符串，如果没获取到返回null
     */
    public String get(String key, String field) {
        Jedis jedis = jedisPool.getResource();
        String value = jedis.hget(key, field);
        jedis.close();
        return value;
    }

    /**
     * 删除对应key的对应field的值
     *
     * @param key key
     * @param field field名
     * @return 成功修改行数
     */
    public Long del(String key, String field) {
        Jedis jedis = jedisPool.getResource();
        Long rowCount = jedis.hdel(key, field);
        jedis.close();
        return rowCount;
    }


    /**
     *  获得对应key的hash列表的映射列表
     *
     * @param key key
     * @return 映射列表
     */
    public Map<String, String> getAll(String key) {
        Jedis jedis = jedisPool.getResource();
        Map<String, String> resultMap = jedis.hgetAll(key);
        jedis.close();
        return resultMap;
    }

}

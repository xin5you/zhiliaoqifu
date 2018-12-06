package com.cn.thinkx.ecom.redis.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class JedisUtilsWithNamespace{

    @Autowired
    private JedisClusterUtils jedis;

    private String namespace;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }


    public String get(String key) {
        key = namespace+key;
        return jedis.get(key);
    }


    public String set(String key, String value, int cacheSeconds) {
        key = namespace+key;
        return jedis.set(key, value, cacheSeconds);
    }


    public String setex(String key, String value, int cacheSeconds) {
        key = namespace+key;
        return jedis.setex(key, value, cacheSeconds);
    }


    public long del(String key) {
        key = namespace+key;
        return jedis.del(key);
    }


    public Boolean exists(String key) {
        key = namespace+key;
        return jedis.exists(key);
    }


    public void expire(String key, int seconds) {
        key = namespace+key;
        jedis.expire(key, seconds);
    }


    public <T> void setList(String key, List<T> list) {
        key = namespace+key;
        jedis.setList(key, list);
    }


    public <T> void setexList(String key, List<T> list, int cacheSeconds) {
        key = namespace+key;
        jedis.setexList(key, list, cacheSeconds);
    }


    public <T> List<T> getList(String key) {
        key = namespace+key;
        return jedis.getList(key);
    }


    public <T> void setMap(String key, Map<String, T> map) {
        key = namespace+key;
        jedis.setMap(key, map);
    }


    public <T> void setexMap(String key, Map<String, T> map, int cacheSeconds) {
        key = namespace+key;
        jedis.setexMap(key, map, cacheSeconds);
    }


    public <T> Map<String, T> getMap(String key) {
        key = namespace+key;
        return jedis.getMap(key);
    }


    public void hset(String key, String field, String value) {
        key = namespace+key;
        jedis.hset(key, field, value);
    }


    public String hget(String key, String field) {
        key = namespace+key;
        return jedis.hget(key, field);
    }


    public void hdel(String key, String field) {
        key = namespace+key;
        jedis.hdel(key, field);
    }


    public void zadd(String key, Map<String, Double> scoreMembers) {
        key = namespace+key;
        jedis.zadd(key, scoreMembers);
    }


    public Map<String, Double> zrange(String key) {
        key = namespace+key;
        return jedis.zrange(key);
    }


    public boolean zrem(String key, Set<String> members) {
        key = namespace+key;
        return jedis.zrem(key, members);
    }


    public void lpush(String key, List<String> values) {
        key = namespace+key;
        jedis.lpush(key, values);
    }


    public List<String> lrange(String key) {
        key = namespace+key;
        return jedis.lrange(key);
    }


    public boolean lremAll(String key) {
        key = namespace+key;
        return jedis.lremAll(key);
    }


    public void sadd(String key, Set<String> member) {
        key = namespace+key;
        jedis.sadd(key, member);
    }


    public Set<String> smembers(String key) {
        key = namespace+key;
        return jedis.smembers(key);
    }


    public boolean srem(String key, Set<String> members) {
        key = namespace+key;
        return jedis.srem(key, members);
    }


    public boolean sismember(String key, String member) {
        key = namespace+key;
        return jedis.sismember(key, member);
    }

    public JedisClusterUtils getJedis() {
        return jedis;
    }

    public void setJedis(JedisClusterUtils jedis) {
        this.jedis = jedis;
    }
}

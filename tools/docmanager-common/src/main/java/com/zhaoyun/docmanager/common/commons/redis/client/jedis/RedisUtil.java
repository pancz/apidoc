/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.redis.client.jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.JedisCluster;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zhaoyun.docmanager.common.commons.objects.SerializeUtils;

/**
 * RedisUtil redis客户端工具类
 * @author user
 * @version $Id: JedisUtil.java, v 0.1 2016年3月18日 下午3:03:32 user Exp $
 */
public class RedisUtil {

    /***
     * 获取JedisCluster
     * @return
     * @date: 2016年3月21日 下午1:55:48
     */
    private static JedisCluster getRedisClient() {
        return JedisClusterFactory.getJedisClient();
    }

    /***
     * 根据key获取指定字符串后用@com.alibaba.fastjson.JSON将其转换为对象
     * @param key
     * @param clazz
     * @return
     * @date: 2016年3月21日 上午11:57:32
     */
    public static <T> T getBean(final String key, final Class<T> clazz) {
        String value = get(key);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return JSON.parseObject(value, clazz);
    }

    /***
     * 根据key(String)获取对应的value(String)
     * @param key
     * @return
     * @date: 2016年3月21日 下午12:01:31
     */
    public static String get(final String key) {
        return getRedisClient().get(key);
    }

    /***
     * 根据key(byte[])获取对应的value(byte[])
     * @param key
     * @return
     * @date: 2016年3月21日 下午12:01:31
     */
    public static byte[] get(final byte[] key) {
        return getRedisClient().get(key);
    }

    /***
     * 获取到bytes后利用@com.zhaoyun.docmanager.common.commons.objects.SerializeUtils进行反序列化转化为Object对象
     * @param key
     * @return
     * @date: 2016年3月21日 下午1:46:25
     */
    public static Object getObject(final String key) {
        byte[] value = get(key.getBytes());
        if (value == null || value.length == 0) {
            return null;
        }
        return SerializeUtils.deserialize(value, Object.class);
    }

    /***
     * 设置指定的在Redis的键的字符串值，并返回其原来的值。中间要将新值和老值用@com.zhaoyun.docmanager.common.commons.objects.SerializeUtils进行转换
     * @param key
     * @param value
     * @return
     * @date: 2016年3月23日 上午10:53:00
     */
    public static Object getSetObject(final String key, final Object value) {
        byte[] oldValue = getSet(key.getBytes(), SerializeUtils.serialize(value));
        if (oldValue == null || oldValue.length == 0) {
            return null;
        }
        return SerializeUtils.deserialize(oldValue, Object.class);
    }

    /***
     * 根据key获取列表对象(获取到单个对象的bytes后利用@com.zhaoyun.docmanager.common.commons.objects.SerializeUtils进行反序列化)
     * @param key
     * @return
     * @date: 2016年3月21日 下午1:53:52
     */
    public static List<Object> getObjectList(final String key) {
        List<byte[]> data = getRedisClient().lrange(key.getBytes(), 0,
            getRedisClient().llen(key.getBytes()));
        List<Object> result = Lists.newArrayList();
        for (byte[] bs : data) {
            result.add(SerializeUtils.deserialize(bs, Object.class));
        }
        return result;
    }

    /***
     * 判断对应的key对象的field是否存在
     * @param key
     * @param field
     * @return
     * @date: 2016年3月21日 下午2:12:17
     */
    public static boolean hexists(String key, String field) {
        return getRedisClient().hexists(key, field);
    }

    /***
     * 对应的key增加field对象
     * @param key
     * @param field
     * @return
     * @date: 2016年3月21日 下午2:12:17
     */
    public static Long hset(String key, String field, Object value) {
        return getRedisClient().hset(key.getBytes(), field.getBytes(),
            SerializeUtils.serialize(value));
    }

    /***
     * 对应的key的field对应的对象
     * @param key
     * @param field
     * @return
     * @date: 2016年3月21日 下午2:12:17
     */
    public static Object hget(String key, String field) {
        return SerializeUtils.deserialize(getRedisClient().hget(key.getBytes(), field.getBytes()),
            Object.class);
    }

    /***
     * 对应的key删除field对象
     * @param key
     * @param field
     * @return
     * @date: 2016年3月21日 下午2:12:17
     */
    public static Long hdel(String key, String field, Object value) {
        return getRedisClient().hdel(key.getBytes(), field.getBytes());
    }

    /***
     * 获取key的全部field对象
     * @param key
     * @param field
     * @return
     * @date: 2016年3月21日 下午2:12:17
     */
    public static Map<String, Object> hgetAll(String key) {
        Map<byte[], byte[]> valueMap = getRedisClient().hgetAll(key.getBytes());
        Map<String, Object> result = new HashMap<String, Object>();
        for (Entry<byte[], byte[]> entry : valueMap.entrySet()) {
            result.put(new String(entry.getKey()),
                SerializeUtils.deserialize(entry.getValue(), Object.class));
        }
        return result;
    }

    /***
     * 利用@com.alibaba.fastjson.JSON将value转换为将String后存储
     * @param key
     * @param value
     * @date: 2016年3月21日 下午2:14:31
     */
    public static String setBean(final String key, final Object value) {
        return set(key, JSON.toJSONString(value));
    }

    /***
     * 将value存储
     * @param key
     * @param value
     * @return
     * @date: 2016年3月21日 下午2:15:28
     */
    public static String set(final String key, final String value) {
        return getRedisClient().set(key, value);
    }

    /***
     * 不存在才设置的key-value对，设置成功后加上过期时间
     * @param key
     * @param value
     * @param seconds
     * @return
     * @date: 2016年3月21日 下午2:30:57
     */
    public static final boolean setnx(final String key, final String value, int seconds) {
        String result = getRedisClient().set(key, value, "NX", "EX",
            seconds);
        return "OK".equalsIgnoreCase(result);
    }

    /***
     * 不存在才设置的key-value对
     * @param key
     * @param value
     * @return
     * @date: 2016年3月21日 下午2:30:57
     */
    public static Long setnx(String key, final String value) {
        return getRedisClient().setnx(key, value);
    }

    /***
     * 设置key-value对,Object会用@com.zhaoyun.docmanager.common.commons.objects.SerializeUtils进行序列化
     * @param key
     * @param value
     * @return
     * @date: 2016年3月21日 下午2:30:57
     */
    public static String setObject(final String key, final Object value) {
        return getRedisClient().set(key.getBytes(), SerializeUtils.serialize(value));
    }

    /***
     * 设置有超期时间（秒）的key-value对,Object会用@com.zhaoyun.docmanager.common.commons.objects.SerializeUtils进行序列化
     * @param key
     * @param value
     * @param seconds
     * @return
     * @date: 2016年3月21日 下午2:30:57
     */
    public static String setObject(final String key, final Object value, final int seconds) {
        return getRedisClient().setex(key.getBytes(), seconds, SerializeUtils.serialize(value));
    }

    /***
     * 增加列表中的key-value对,Object会用@com.zhaoyun.docmanager.common.commons.objects.SerializeUtils进行序列化
     * @param key
     * @param value
     * @return
     * @date: 2016年3月21日 下午2:30:57
     */
    public static Long setListObject(final String key, final Object value) {
        return getRedisClient().rpush(key.getBytes(), SerializeUtils.serialize(value));
    }

    /***
     * 增加列表中的的key-value对,Object会用@com.zhaoyun.docmanager.common.commons.objects.SerializeUtils进行序列化,增加成功后会修改整个list的过期时间
     * @param key
     * @param value
     * @param seconds
     * @return
     * @date: 2016年3月21日 下午2:30:57
     */
    public static boolean setListObject(final String key, final Object value, int seconds) {
        Long result = getRedisClient().rpush(key.getBytes(), SerializeUtils.serialize(value));
        if ((result != null) && (result.intValue() > 0)) {
            expire(key, seconds);
            return true;
        }
        return false;
    }

    /***
     * 设置有超期时间（秒）的key-value对,将对象用@com.alibaba.fastjson.JSON将其转换为字符串后再进行存储
     * @param key
     * @param value
     * @param seconds
     * @date: 2016年3月21日 下午2:51:06
     */
    public static String setBean(final String key, final Object value, final int seconds) {
        return set(key, JSON.toJSONString(value), seconds);
    }

    /***
     * 设置有超期时间（秒）的key-value对存储
     * @param key
     * @param value
     * @param seconds
     * @date: 2016年3月21日 下午2:51:06f
     */
    public static String set(final String key, final String value, final int seconds) {
        return getRedisClient().setex(key, seconds, value);
    }

    /***
     * 删除key对应的数据对
     * @param key
     * @return
     * @date: 2016年3月21日 下午3:00:09
     */
    public static boolean remove(final String key) {
        return remove(key, null);
    }

    /***
     * 删除key对应的数据对象对
     * @param key
     * @return
     * @date: 2016年3月21日 下午3:00:09
     */
    public static Long removeObject(final String key) {
        return getRedisClient().del(key.getBytes());
    }

    /***
     * 删除key对应的数据对,如果实现了@DeletePermission接口，则对应的allows返回true可以删除，否则不允许删除
     * @param key
     * @param permission
     * @return
     * @date: 2016年3月21日 下午2:58:08
     */
    public static boolean remove(final String key, final DeletePermission permission) {
        if ((permission != null) && !permission.allows(getRedisClient().get(key))) {
            return false;
        }
        Long result = getRedisClient().del(key);
        if (result != null && result > 0) {
            return true;
        }
        return false;
    }

    /***
     * 删除允许接口
     * 
     * @author user
     * @version $Id: RedisUtil.java, v 0.1 2016年3月21日 下午2:57:52 user Exp $
     */
    public interface DeletePermission {
        /**
         * 对应的cache是否允许删除
        * @param cache
        * @return
        */
        public boolean allows(String cache);
    }

    /***
     * 设置指定的在Redis的键的字符串值，并返回其原来的值。中间要讲新值和老值用@com.alibaba.fastjson.JSON进行转换
     * @param key
     * @param value
     * @param getBeanclazz
     * @return
     * @date: 2016年3月21日 下午3:06:25
     */
    public static <T> T getSetBean(final String key, final T value, final Class<T> getBeanclazz) {
        String oldValue = getSet(key, JSON.toJSONString(value));
        if (StringUtils.isBlank(oldValue)) {
            return null;
        }
        return JSON.parseObject(oldValue, getBeanclazz);
    }

    /***
     * 设置指定的在Redis的键的字符串值，并返回其原来的值。
     * @param key
     * @param value
     * @return
     * @date: 2016年3月21日 下午3:02:58
     */
    public static String getSet(final String key, final String value) {
        return getRedisClient().getSet(key, value);
    }

    /***
     * 设置指定的在Redis的键的byte数组，并返回其原来的值。
     * @param key
     * @param value
     * @return
     * @date: 2016年3月21日 下午3:02:58
     */
    public static byte[] getSet(final byte[] key, final byte[] value) {
        return getRedisClient().getSet(key, value);
    }

    /***
     * 原子递增key的整数值，key=key+1。如果该key不存在，执行操作之前它被设置为0。
     * @param key
     * @return
     * @date: 2016年3月21日 下午3:10:22
     */
    public static Long incr(final String key) {
        return getRedisClient().incr(key);
    }

    /***
     * 原子递增key的整数值，key=key+value。如果该key不存在，执行操作之前它被设置为0。
     * @param key
     * @return
     * @date: 2016年3月21日 下午3:10:22
     */
    public static Long incrBy(final String key, final long value) {
        return getRedisClient().incrBy(key, value);
    }

    /***
     * 原子递减key的整数值，key=key-1。如果该key不存在，执行操作之前它被设置为0。
     * @param key
     * @return
     * @date: 2016年3月21日 下午3:30:33
     */
    public static Long decr(final String key) {
        return getRedisClient().decr(key);
    }

    /***
     * 原子递减key的整数值，key=key-value。如果该key不存在，执行操作之前它被设置为0。
     * @param key
     * @return
     * @date: 2016年3月21日 下午3:10:22
     */
    public static Long decrBy(final String key, final long value) {
        return getRedisClient().decrBy(key, value);
    }

    /***
     * 设置超时时间
     * @param key
     * @param seconds
     * @return
     * @date: 2016年3月21日 下午3:35:27
     */
    public static Long expire(final String key, final int seconds) {
        return getRedisClient().expire(key, seconds);
    }

    /***
     * 删除缓存
     * @param key
     * @date: 2016年3月21日 下午3:35:37
     */
    public static Long removeCache(String key) {
        return getRedisClient().del(key);
    }

    /***
     * 判断是否存在
     * @param key
     * @return
     * @date: 2016年3月21日 下午3:36:19
     */
    public static Boolean exist(final String key) {
        return getRedisClient().exists(key);
    }

    /***
     * 判断value是否存在于与key相关的集合中
     * @param key
     * @param value
     * @return
     * @date: 2016年3月21日 下午3:42:36
     */
    public static Boolean sismember(final String key, final String value) {
        return getRedisClient().sismember(key, value);
    }

    /***
     * 从key相关的集合中将value移除
     * @param key
     * @param value
     * @return
     * @date: 2016年3月21日 下午3:45:40
     */
    public static Long srem(final String key, final String value) {
        return getRedisClient().srem(key, value);
    }

    /***
     * 在key相关的集合中增加value
     * @param key
     * @param value
     * @return
     * @date: 2016年3月21日 下午3:46:41
     */
    public static Long sadd(final String key, final String value) {
        return getRedisClient().sadd(key, value);
    }

    /***
     * 批量删除key-value对
     * @param keys
     * @return
     * @date: 2016年3月21日 下午3:49:34
     */
    public static Long removeKeys(final String... keys) {
        return getRedisClient().del(keys);
    }

    /***
     * 获取键到期的剩余时间(秒)
     * @param key
     * @return
     * @date: 2016年3月21日 下午7:18:58
     */
    public static Long ttl(final String key) {
        return getRedisClient().ttl(key);
    }

}

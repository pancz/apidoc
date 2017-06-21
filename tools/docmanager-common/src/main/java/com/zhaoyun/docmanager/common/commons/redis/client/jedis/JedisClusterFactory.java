/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.redis.client.jedis;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * JedisClient的工厂方法，获取BinaryJedisCluster对象
 * @author user
 * @version $Id: RedisClientFactory.java, v 0.1 2016年3月21日 上午9:32:04 user Exp $
 */
public class JedisClusterFactory {

    //jedisClient对象
    private static JedisCluster jedisClient = null;

    /***
     * 根据redisUrl生成Set<HostAndPort>
     * @param redisUrl
     * @return
     * @date: 2016年3月18日 下午4:01:14
     */
    private static Set<HostAndPort> getHostAndPorts(String redisUrl) {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        redisUrl = redisUrl.replaceAll(",", ";");
        String[] redisPerUrls = redisUrl.split(";");
        for (String redisPerUrl : redisPerUrls) {
            String[] hostAndPorts = redisPerUrl.split(":");
            jedisClusterNodes.add(new HostAndPort(hostAndPorts[0].trim(), Integer
                .valueOf(hostAndPorts[1].trim())));
        }
        return jedisClusterNodes;
    }

    /***
     * 根据redisUrl地址生成R=JedisClient对象
     * @param redisUrl ip:port,ip:port,ip:port格式
     * @return
     * @date: 2016年3月18日 下午3:14:18
     */
    public static JedisCluster getJedisClient() {
        if (jedisClient != null) {
            return jedisClient;
        } else {
            synchronized (JedisClusterFactory.class) {
                if (jedisClient != null) {
                    return jedisClient;
                } else {
                    String redis_url = System.getProperty("redis_url");
                    if(isBlank(redis_url)){
                        redis_url = "redis1.cluster.zhaoyunnb.com:6379,redis2.cluster.zhaoyunnb.com:6379,redis3.cluster.zhaoyunnb.com:6379";
                    }
                    jedisClient = new JedisCluster(getHostAndPorts(redis_url));
                    return jedisClient;
                }
            }
        }
    }
}

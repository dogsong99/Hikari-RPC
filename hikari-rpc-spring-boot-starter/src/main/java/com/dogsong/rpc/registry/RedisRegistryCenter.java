package com.dogsong.rpc.registry;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis 注册中心
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/28
 */
@Deprecated
public class RedisRegistryCenter {

    /** 非切片额客户端连接 */
    private static Jedis jedis;

    /** 初始化redis */
    public static void init(String host, int port) {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(5);
        config.setTestOnBorrow(false);
        JedisPool jedisPool = new JedisPool(config, host, port);
        jedis = jedisPool.getResource();
    }

    /**
     * 注册生产者
     *
     * @param nozzle 接口
     * @param alias  别名
     * @param info   信息
     * @return Long
     */
    public static Long registryProvider(String nozzle, String alias, String info) {
        return jedis.sadd(nozzle + "_" + alias, info);
    }

    /**
     * 获取生产者
     * 模拟权重，随机获取
     *
     * @param nozzle 接口
     * @param alias  别名
     * @return String
     */
    public static String obtainProvider(String nozzle, String alias) {
        return jedis.srandmember(nozzle + "_" + alias);
    }

    public static Jedis jedis() {
        return jedis;
    }

}

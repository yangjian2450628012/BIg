package tech.yobbo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;

/**
 * Created by xiaoJ on 5/22/2017.
 */
public class CacheUtils {
    private static final Logger  log = LoggerFactory.getLogger(CacheUtils.class);
    private static JedisPool jedisPool;
    private static Jedis jedis;

    private static Jedis getJedis() {
        try {
            jedisPool = JedisFactory.getInstance().getJedisPool();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        jedis = jedisPool.getResource();
        return jedis;
    }

    /**
     * 初始化redis缓存
     */
    public static void init(){

    }
}

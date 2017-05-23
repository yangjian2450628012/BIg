package tech.yobbo.util;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import tech.yobbo.config.common.SystemConstant;

import java.io.IOException;

/**
 * Created by xiaoJ on 5/22/2017.
 */
public class JedisFactory {
    private static JedisPool jedisPool;
    private static JedisFactory instance = null;

    public JedisFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        String host = SystemConstant.getConfig("redis.host");
        int port = Integer.parseInt(SystemConstant.getConfig("redis.port"));
        int timeout = Integer.parseInt(SystemConstant.getConfig("redis.timeout"));
        String password = SystemConstant.getConfig("redis.password");
        if (password.equals("")) password = null;
        int database = Integer.parseInt(SystemConstant.getConfig("redis.database"));
        jedisPool = new JedisPool(poolConfig, host, port, timeout, password, database);
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public static JedisFactory getInstance() throws IOException {

        if (instance == null) {
            instance = new JedisFactory();
        }
        return instance;
    }
}

package tech.yobbo.config.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by xiaoJ on 5/22/2017.
 */
public class SystemConstant {
    private static final Logger log = LoggerFactory.getLogger(SystemConstant.class);
    private static final String CONFIG_FILE = "database.properties"; // 配置文件
    private static Properties config;

    /**
     * 加载配置文件到Config中
     */
    private static void loadConfig() {
        if (config == null) {
            config = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try {
                config.load(loader.getResourceAsStream(CONFIG_FILE));
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 获取配置文件属性值
     * @param key
     * @return
     */
    public static String getConfig(String key)  {
        if (config == null) {
            loadConfig();
        }
        return config.getProperty(key);
    }

}

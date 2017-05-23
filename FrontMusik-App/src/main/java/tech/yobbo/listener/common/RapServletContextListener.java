package tech.yobbo.listener.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.yobbo.util.CacheUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 缓存监听
 * Created by xiaoJ on 5/22/2017.
 */
public class RapServletContextListener implements ServletContextListener{
    private static final Logger logger = LoggerFactory.getLogger(RapServletContextListener.class);

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("FrontMusik-App Server initializing...");

        logger.info("Initializing Jedis Server...");
        CacheUtils.init();

        logger.info("FrontMusik-App Server ready.");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("Context destroyed.");
    }
}

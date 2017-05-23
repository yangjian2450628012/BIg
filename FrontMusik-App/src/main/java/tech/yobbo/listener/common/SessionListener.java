package tech.yobbo.common.listener;

import tech.yobbo.util.ContextManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Map;

/**
 * 统计用户在线人数
 * Created by xiaoJ on 5/22/2017.
 */
public class SessionListener implements HttpSessionListener{

    /**
     * 当创建一个session时，在线用户数加1
     * @param httpSessionEvent
     */
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        ServletContext context = httpSessionEvent.getSession().getServletContext();
        // 统计在线用户人数
        String key = ContextManager.KEY_COUNT_OF_ONLINE_USER_LIST;
        if (context.getAttribute(key) == null) {
            context.setAttribute(key,0);
        }
        int curCountOfOnlineuserList = (Integer) context.getAttribute(key);
        context.setAttribute(key,curCountOfOnlineuserList + 1);
    }

    /**
     * 当销毁一个session时，在线用户数减1
     * @param httpSessionEvent
     */
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        ServletContext context = httpSessionEvent.getSession().getServletContext();
        HttpSession session = httpSessionEvent.getSession();

        // 统计在线用户人数
        String key = ContextManager.KEY_COUNT_OF_ONLINE_USER_LIST;
        if (context.getAttribute(key) == null) {
            context.setAttribute(key,0);
        }
        int curCountOfOnlineUserList = (Integer) context.getAttribute(key);
        if (curCountOfOnlineUserList < 1) curCountOfOnlineUserList = 1;
        context.setAttribute(key,curCountOfOnlineUserList);

        Object userObjId = session.getAttribute(ContextManager.KEY_USER_ID);
        if (userObjId != null) {
            long userId = (Long)userObjId;
            Object projectLockListObj = context.getAttribute(ContextManager.KEY_PROJECT_LOCK_LIST);
            if (projectLockListObj != null) {
                Map projectLockList = (Map)projectLockListObj;
                if (projectLockList.containsKey(userId)) {
                    projectLockList.remove(userId);
                    context.setAttribute(ContextManager.KEY_PROJECT_LOCK_LIST,projectLockList);
                }
            }
        }

    }
}

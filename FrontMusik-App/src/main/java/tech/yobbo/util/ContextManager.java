package tech.yobbo.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaoJ on 5/22/2017.
 */
public class ContextManager {
    public static final String KEY_COUNT_OF_ONLINE_USER_LIST = "KEY_COUNT_OF_ONLINE_USER_LIST";
    public static final String KEY_ACCOUNT = "KEY_ACCOUNT";
    public static final String KEY_PROJECT_LOCK_LIST = "KEY_PROJECT_LOCK_LIST";
    public static final String KEY_USER_ID = "KEY_USER_ID";
    // public static final String KEY_CORP_NAME = "KEY_CORP_NAME";
    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_ROLE_LIST = "KEY_ROLE_LIST";
    public static final String KEY_EMP_ID = "KEY_EMP_ID";


    /**
     * 把session放到map中
     * @return
     */
    public static Map currentSession() {
        Map<String ,Object> map = new HashMap<String, Object>();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession sessions = request.getSession();
        Enumeration<String> sessionKeys = sessions.getAttributeNames();
        if (sessionKeys.hasMoreElements()) {
            String key = sessionKeys.nextElement();
            map.put(key,sessions.getAttribute(key));
        }
        return map;
    }

    /**
     * 把session放到map中
     * @return
     */
    public static Map currentCookie(){
        Map<String,Object> map = new HashMap<String, Object>();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        for (int i=0;i<cookies.length;i++) {
            Cookie cookie = cookies[i];
            map.put(cookie.getName(),cookie.getValue());
        }
        return map;
    }

    public static Map getApplication() {
//        return ActionContext.getContext().getApplication();
        return null;
    }
}

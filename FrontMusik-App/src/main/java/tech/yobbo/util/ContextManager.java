package tech.yobbo.util;

import org.apache.struts.chain.contexts.ActionContext;

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

    @SuppressWarnings("rawtypes")
    public static Map currentSession() {
//        return ActionContext.getContext().getServletContextssion();
        return null;
    }

    @SuppressWarnings("rawtypes")
    public static Map getApplication() {
//        return ActionContext.getContext().getApplication();
        return null;
    }
}

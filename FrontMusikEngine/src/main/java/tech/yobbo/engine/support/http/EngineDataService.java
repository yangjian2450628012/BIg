package tech.yobbo.engine.support.http;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import tech.yobbo.engine.support.data.EngineDataManagerFacade;

/** 
 * engine业务逻辑处理类，主要包括数据库连接池的获取和转发前端发送请求到业务实现类
 * Created by xiaoJ on 6/1/2017.
 *
 */
public class EngineDataService extends EngineDataServiceHelp {
    private static  EngineDataService instance                      = null;
    private DataSource dataSource                                   = null;
    private String dataSource_className                             = null;
    private static EngineDataManagerFacade engineDataManagerFacade  = EngineDataManagerFacade.getInstance(); //获取具体数据操作类

    private EngineDataService(){}
    public static EngineDataService getInstance(){
        if (instance == null) {
            instance = new EngineDataService();
        }
        return instance;
    }

	/**
	 * 获取模板中数据
	 * @param url 前端发送的请求
	 * @param parameters URL中的参数列表
	 * @param context ServletContext上下文
	 * @return 返回模板中需要的数据
	 */
	public Object processTemplate(String url,Map<String, String> parameters,ServletContext context){
        if(dataSource == null){
            setDataSource(context);
        }
        if (url.startsWith("/index.html")) { //首页信息
            return  engineDataManagerFacade.getBasicInfo(parameters);
        }else if(url.startsWith("/code.html")){
        	return engineDataManagerFacade.getCodeInfo(parameters);
        }
        return null;
	}

	/**
     * 调用服务
     * @param url
     * @param context 上下文
     * @return
     */
    public String process(String url,ServletContext context){
        Map<String, String> parameters = getParameters(url);
        if (dataSource == null) {
            setDataSource(context);
        }
        if (url.startsWith("/index.json")) {
            return returnJSONResult(RESULT_CODE_SUCCESS, engineDataManagerFacade.getIndexList(parameters));
        } else if(url.startsWith("/tree.json")){
            return returnJSONResult(RESULT_CODE_SUCCESS,engineDataManagerFacade.getTemplateTree(parameters));
        }
        return returnJSONResult(RESULT_CODE_ERROR, "Do not support this request, please contact with administrator.");
    }


    /**
     * 通过反射获取spring中dataSource连接池bean
     * 避免没导包，导致运行报错
     * @param context
     */
    public void setDataSource(ServletContext context){
    	if(EngineViewServlet.getDataSource() == null) return;
        System.out.println("初始化数据库连接池！");
        try {
        	// 通过反射获取spring中的连接池
			Class<?> _class = Class.forName("org.springframework.web.context.support.WebApplicationContextUtils");
			Method method  = _class.getMethod("getRequiredWebApplicationContext", ServletContext.class);
            Object ctx = method.invoke(method.getReturnType(), context);
            Method m;
            if(EngineViewServlet.getDataSource().contains(".")){
                m = ctx.getClass().getMethod("getBean", Class.class);
                dataSource = (DataSource) m.invoke(ctx
                        , Class.forName(EngineViewServlet.getDataSource()));
                dataSource_className = EngineViewServlet.getDataSource();
            }else{
                m = ctx.getClass().getMethod("getBean",String.class);
                dataSource = (DataSource) m.invoke(ctx, EngineViewServlet.getDataSource());
                dataSource_className = dataSource.getClass().getName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }
    public String getDataSource_className() {
        return dataSource_className;
    }
}

package tech.yobbo.engine.support.http;

import tech.yobbo.engine.support.data.EngineDataManagerFacade;
import tech.yobbo.engine.support.util.JdbcUtils;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/** 
 * engine业务逻辑处理类，主要包括数据库连接池的获取和转发前端发送请求到业务实现类
 * Created by xiaoJ on 6/1/2017.
 *
 */
public class EngineDataService extends EngineDataServiceHelp {
    private static  EngineDataService instance                      = null;
    private DataSource dataSource                                   = null;
    private String dataSource_className                             = null;
    private Date startTime											= null;
    private String db_type                                          = null;
    private String jar_path                                         = null;
    private static EngineDataManagerFacade engineDataManagerFacade  = EngineDataManagerFacade.getInstance(); //获取具体数据操作类

    private EngineDataService(){}
    public static EngineDataService getInstance(){
        if (instance == null) {
            instance = new EngineDataService();
        }
        return instance;
    }

    /**
     * 初始化自动化引擎，包括创建相应依赖表
     */
    protected  void init(){
        if(this.dataSource == null) return;
        // 获取连接池信息，判断数据库类型
        JdbcUtils jdbcUtils = JdbcUtils.getInstance();
        try {
            jdbcUtils.getConnection(this.dataSource);
            String sql_version = "select version() from dual";
            String oracle_version = "select * from v$version";
            if(jdbcUtils.getDataBySql(oracle_version).size() > 0){
                db_type = "oracle";
            }else if(jdbcUtils.getDataBySql(sql_version).size() > 0){
                db_type = "mysql";
            }
            if (db_type != null) {
                String path = Thread.currentThread().getContextClassLoader().getResource("/").getPath();
                System.out.println(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                jdbcUtils.closeDb();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

	/**
	 * 获取模板中数据,返回给模板
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
     * 调用服务，ajax异步相关的，只要师ajax请求都会走process这个方法去处理
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
            return returnJSONResult(RESULT_CODE_SUCCESS, engineDataManagerFacade.getIndexList());
        } else if(url.startsWith("/tree.json")){
            return returnJSONResult(RESULT_CODE_SUCCESS,engineDataManagerFacade.getTemplateTree(parameters));
        }
        return returnJSONResult(RESULT_CODE_ERROR, "Do not support this request, please contact with administrator.");
    }


    /**
     * 通过反射获取spring中dataSource连接池bean
     * 避免没导包，导致运行报错
     * @param context servletContext上下文
     */
    protected void setDataSource(ServletContext context){
    	if(EngineViewServlet.getDataSource() == null) return;
        System.out.println("初始化数据库连接池！");
        try {
        	// 通过反射获取spring中的连接池
			Class<?> _class = Class.forName("org.springframework.web.context.support.WebApplicationContextUtils");
			Method method  = _class.getMethod("getRequiredWebApplicationContext", ServletContext.class);
            Object ctx = method.invoke(method.getReturnType(), context);
           
            if(EngineViewServlet.getDataSource().contains(".")){
            	Method m = ctx.getClass().getMethod("getBean", Class.class);
                dataSource = (DataSource) m.invoke(ctx
                        , Class.forName(EngineViewServlet.getDataSource()));
                dataSource_className = EngineViewServlet.getDataSource();
            }else{
            	Method m = ctx.getClass().getMethod("getBean",String.class);
                dataSource = (DataSource) m.invoke(ctx, EngineViewServlet.getDataSource());
                dataSource_className = dataSource.getClass().getName();
            }
            Method m = ctx.getClass().getMethod("getStartupDate");
            Long startUpDate = (Long)m.invoke(ctx);
            startTime = new Date(startUpDate);
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
    public Date getStartTime(){
    	return startTime;
    }
}

package tech.yobbo.engine.support.http;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import tech.yobbo.engine.support.util.JdbcUtils;
import tech.yobbo.engine.support.util.Utils;
import tech.yobbo.engine.support.util.VERSION;

/**
 * Created by xiaoJ on 6/1/2017.
 *
 */
public class EngineDataService extends EngineDataServiceHelp {
    private static  EngineDataService instance      = null;
    private static DataSource dataSource            = null;

    private EngineDataService(){}
    public static EngineDataService getInstance(){
        if (instance == null) {
            instance = new EngineDataService();
        }
        return instance;
    }

	// 获取模板中数据
	public Object processTemplate(String url,Map<String, String> parameters,ServletContext context){
        if(dataSource == null){
            setDataSource(context);
        }
        if (url.startsWith("/index.html")) { //首页信息
            return  getBasicInfo(parameters);
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
        if (url.startsWith(INDEX_URL)) {
            return returnJSONResult(RESULT_CODE_SUCCESS, getIndexList(parameters));
        }
        return returnJSONResult(RESULT_CODE_ERROR, "Do not support this request, please contact with administrator.");
    }

    protected void setDataSource(ServletContext context){
        System.out.println("当前没有数据连接池，要获取连接池");
        // 获取spring中的连接池
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        try {
            dataSource = (DataSource) ctx.getBean(Class.forName(EngineViewServlet.getDataSource()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取首页列表
    private static List getIndexList(Map<String, String> parameters) {
        try {
            JdbcUtils jdbcUtils = JdbcUtils.getInstance();
            jdbcUtils.getConnection(dataSource);
            List data = jdbcUtils.getDataBySql(INDEX_SQL,new Object[]{0,100000});
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取首页基础数据
    private static Map getBasicInfo(Map<String,String> params){
        Map<String,Object> dataMap = new LinkedHashMap();
        dataMap.put("Version", VERSION.getVersionNumber());
        dataMap.put("base_path",params.get("base_path"));
        dataMap.put("package_name",params.get("package_name"));
        dataMap.put("dataSource",params.get("dataSource"));
        dataMap.put("Drivers", params.get("dataSource"));
        dataMap.put("JavaVMName", System.getProperty("java.vm.name"));
        dataMap.put("JavaVersion", System.getProperty("java.version"));
        dataMap.put("StartTime", Utils.getStartTime());
        return dataMap;
    }
}

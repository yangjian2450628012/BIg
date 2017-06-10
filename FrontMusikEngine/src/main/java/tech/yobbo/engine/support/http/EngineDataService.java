package tech.yobbo.engine.support.http;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import tech.yobbo.engine.support.util.JdbcUtils;
import tech.yobbo.engine.support.util.Utils;
import tech.yobbo.engine.support.util.VERSION;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by xiaoJ on 6/1/2017.
 *
 */
public class EngineDataService extends EngineDataServiceHelp {
    private static  EngineDataService instance      = null;
    private static DataSource dataSource            = null;
    private static String dataSource_className      = null;

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
        System.out.println("初始化数据库连接池！");
        // 获取spring中的连接池
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        try {
            dataSource = (DataSource) ctx.getBean(Class.forName(EngineViewServlet.getDataSource()));
            dataSource_className = dataSource.getClass().getName();
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
//  jar       file:/E:/公司项目源码/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/das/WEB-INF/lib/engine-1.0.0.jar!/engine/http/resources/template
//  类        /E:/电影网站模板/FrontMusik/FrontMusikEngine/target/FrontMusik-Engine/WEB-INF/classes/engine/http/resources/template/
        dataMap.put("temlate_path", Thread.currentThread().getContextClassLoader().getResource(EngineViewServlet.RESOURCE_PATH +"/template").getPath());
//  jar       jar:file:/E:/公司项目源码/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/das/WEB-INF/lib/engine-1.0.0.jar!/engine/http/resources/template
//  类        file:/E:/电影网站模板/FrontMusik/FrontMusikEngine/target/FrontMusik-Engine/WEB-INF/classes/engine/http/resources/template/
        dataMap.put("common_path", Thread.currentThread().getContextClassLoader().getResource(EngineViewServlet.RESOURCE_PATH +"/template"));
        dataMap.put("base_path",params.get("base_path"));
        dataMap.put("package_name",params.get("package_name"));
        dataMap.put("dataSource",dataSource_className);
        dataMap.put("Drivers", params.get("dataSource"));
        dataMap.put("JavaVMName", System.getProperty("java.vm.name"));
        dataMap.put("JavaVersion", System.getProperty("java.version"));
        dataMap.put("StartTime", Utils.getStartTime());
        return dataMap;
    }

    public static void  main(String[] arg){
        String path = "file:E:\\公司项目源码\\das\\WebContent\\WEB-INF\\lib\\engine-1.0.0.jar!/engine/http/resources/template";
        //path = "/E:/电影网站模板/FrontMusik/FrontMusikEngine/target/FrontMusik-Engine/WEB-INF/classes/engine/http/resources/template/";
        if (path.startsWith("file:") && path.indexOf("file:") != -1) {
            path = path.substring(5,path.length());
        }
        if (path.indexOf(".jar!") != -1) {
            path = path.substring(0,path.indexOf(".jar")+4);
        }
        System.out.println("path:   "+path);
        File file = new File(path);

        System.out.println(file.exists());
        try {
            JarFile jar = new JarFile(path);
            Enumeration enums = jar.entries();
            while(enums.hasMoreElements()){
                JarEntry entry = (JarEntry) enums.nextElement();
                if(entry.getName().startsWith("engine/http/resources/template")){
                    jar.getInputStream(entry); //获取流
                    System.out.println("enti:  "+ entry.getName()+",是否是目录: "+entry.isDirectory());
                }
            }
            System.out.println(jar.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

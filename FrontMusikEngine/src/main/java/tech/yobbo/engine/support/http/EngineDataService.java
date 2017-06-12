package tech.yobbo.engine.support.http;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import tech.yobbo.engine.support.json.JSONParser;
import tech.yobbo.engine.support.json.JSONUtils;
import tech.yobbo.engine.support.json.JSONWriter;
import tech.yobbo.engine.support.util.JdbcUtils;
import tech.yobbo.engine.support.util.StringUtils;
import tech.yobbo.engine.support.util.Utils;
import tech.yobbo.engine.support.util.VERSION;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
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
        if (url.startsWith("/index.json")) {
            return returnJSONResult(RESULT_CODE_SUCCESS, getIndexList(parameters));
        } else if(url.startsWith("/tree.json")){
            return returnJSONResult(RESULT_CODE_SUCCESS,getTemplateTree(parameters));
        }
        return returnJSONResult(RESULT_CODE_ERROR, "Do not support this request, please contact with administrator.");
    }

    /**
     * 获取模板中树形菜单
     * @param parameters
     * @return
     */
    private Object getTemplateTree(Map<String, String> parameters) {
        String jar_path = parameters.get("template_path");
        if (jar_path.startsWith("file:") && jar_path.indexOf("file:") != -1) {
            jar_path = jar_path.substring(5,jar_path.length());
        }
        if (jar_path.indexOf(".jar") != -1) {
            jar_path = jar_path.substring(0,jar_path.indexOf(".jar")+4);
        }
        System.out.println(jar_path);

        return null;
    }

    /**
     * 通过反射获取spring中dataSource连接池bean
     * 避免没导包，导致运行报错
     * @param context
     */
    protected void setDataSource(ServletContext context){
    	if(EngineViewServlet.getDataSource() == null) return;
        System.out.println("初始化数据库连接池！");
        try {
        	// 通过反射获取spring中的连接池
			Class<?> _class = Class.forName("org.springframework.web.context.support.WebApplicationContextUtils");
			Method method  = _class.getMethod("getRequiredWebApplicationContext", ServletContext.class);
            Object ctx = method.invoke(method.getReturnType(), context);
			Method m = ctx.getClass().getMethod("getBean", Class.class);
			dataSource = (DataSource) m.invoke(ctx
					, Class.forName(EngineViewServlet.getDataSource()));
            dataSource_className = EngineViewServlet.getDataSource();
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
//        dataMap.put("temlate_path", Thread.currentThread().getContextClassLoader().getResource(EngineViewServlet.RESOURCE_PATH +"/template").getPath());
//  jar       jar:file:/E:/公司项目源码/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/das/WEB-INF/lib/engine-1.0.0.jar!/engine/http/resources/template
//  类        file:/E:/电影网站模板/FrontMusik/FrontMusikEngine/target/FrontMusik-Engine/WEB-INF/classes/engine/http/resources/template/
        dataMap.put("common_path", "");

        dataMap.put("template_path", "file:/D:/engineJar/engine-1.0.0.jar!/engine/http/resources/template");
        dataMap.put("treeUrl","tree.json?template_path="+dataMap.get("template_path"));
        dataMap.put("base_path",params.get("base_path"));
        dataMap.put("package_name",params.get("package_name"));
        dataMap.put("dataSource",dataSource_className);
        dataMap.put("Drivers", params.get("dataSource"));
        dataMap.put("JavaVMName", System.getProperty("java.vm.name"));
        dataMap.put("JavaVersion", System.getProperty("java.version"));
        dataMap.put("StartTime", Utils.getStartTime());
        return dataMap;
    }

    // 递归组织树形菜单
    private static List<Map<String,Object>> recursionDirectory(List<String> data,String start){
        if(data == null || data.size() ==0) return null;
        List<Map<String,Object>> _d = new ArrayList<Map<String, Object>>();
        for(int i=0;i<data.size();i++){
            System.out.println(start +" | " + data.get(i));
            if(start.startsWith(data.get(i))){
                System.out.println("common in...");
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("name",data.get(i).replaceFirst(start,""));
                if(data.get(i).endsWith(".ftl") || data.get(i).equals("/")) {
                    map.put("url",data.get(i));
                }/*else{
                    String name = map.get("name").toString();
                    name = name.substring(0,name.length()-1);
                    map.remove("name");
                    map.put("name",name);
                }*/
                map.put("id",UUID.randomUUID().toString().replaceAll("-",""));
                data.remove(i);
                try{
                    List<Map<String,Object>> list_child = recursionDirectory(data,data.get(i+1));
                    if(list_child != null && list_child.size() > 0){
                        map.put("children",list_child);
                    }
                } catch (Exception e){

                }
                _d.add(map);
            }
        }
        return _d;
    }

    public static void  main(String[] arg){
        String prefix = EngineViewServlet.RESOURCE_PATH + "/template";
        String path = "file:/D:/engineJar/engine-1.0.0.jar!/engine/http/resources/template";
        if (path.startsWith("file:") && path.indexOf("file:") != -1) {
            path = path.substring(5,path.length());
        }
        if (path.indexOf(".jar") != -1) {
            path = path.substring(0,path.indexOf(".jar")+4);
        }
//        System.out.println("path:   "+path);

        try {
            List<String> data = new ArrayList<String>();
            JarFile jar = new JarFile(path);
            Enumeration enums = jar.entries();
            while(enums.hasMoreElements()){
                JarEntry entry = (JarEntry) enums.nextElement();
                if(entry.getName().startsWith(prefix)){
                    data.add(entry.getName().replaceAll(prefix,""));
                    System.out.println(entry.getName().replaceAll(prefix,""));
                }
            }
            List<Map<String,Object>> s = recursionDirectory(data,"/");
            for(int i=0;i<s.size();i++){
                System.out.println(s.get(i));
            }
            String r = JSONUtils.toJSONString(s);
            System.out.println(r);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

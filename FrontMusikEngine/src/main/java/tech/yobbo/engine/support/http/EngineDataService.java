package tech.yobbo.engine.support.http;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import tech.yobbo.engine.support.json.JSONUtils;
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
        }else if(url.startsWith("/code.html")){
        	return getCodeInfo(parameters);
        }
        return null;
	}
	
	// 获取code中的数据
    private Map getCodeInfo(Map<String, String> parameters) {
    	// TODO 后期完善
		return parameters;
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
    private String getTemplateTree(Map<String, String> parameters) {
        String jar_path = parameters.get("template_path");
        String prefix = EngineViewServlet.RESOURCE_PATH + "/template";
        if (jar_path.startsWith("file:") && jar_path.indexOf("file:") != -1) {
            jar_path = jar_path.substring(5,jar_path.length());
        }
        if (jar_path.indexOf(".jar") != -1) {
            jar_path = jar_path.substring(0,jar_path.indexOf(".jar")+4);
        }
		try {
			// 获取jar中的列表
			List<String> data = new ArrayList<String>();
			JarFile jar = new JarFile(jar_path);
			Enumeration enums = jar.entries();
	        while(enums.hasMoreElements()){
	            JarEntry entry = (JarEntry) enums.nextElement();
	            if(entry.getName().startsWith(prefix)){
	                data.add(entry.getName().replaceAll(prefix,""));
	            }
	        }
	        List list = recursionDirectory(data, "/");
	        return JSONUtils.toJSONString(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
    	List<Map<String,Object>> _d = new ArrayList<Map<String,Object>>();
    	for(int i=0;i<data.size();i++){
    		String pattern = "^"+start+"[A-Za-z0-9_]+/$";
    		String ftlPattern = "^"+start+"([A-Za-z0-9_]+).ftl$";
//    		System.out.println("pattern："+pattern);
//    		System.out.println("ftlPattern："+ftlPattern);
    		Pattern r = Pattern.compile(pattern);
    		Pattern ftl_r = Pattern.compile(ftlPattern);
    		Map<String,Object> map = new HashMap<String, Object>();
    		if(r.matcher(data.get(i+1)).find()){
    			map.put("name", data.get(i+1));
    		}else if(ftl_r.matcher(data.get(i+1)).find()){
    			map.put("name", data.get(i+1));
				_d.add(map);
				return _d;
    		}else {
    			map.put("name", data.get(i+1));
				_d.add(map);
				return _d;
    		}
    		List<Map<String,Object>> data_child = recursionDirectory(data,data.get(i+1));
    		if(data_child != null && data_child.size() > 0){
    			map.put("children", data_child);
    			_d.add(map);
    		}
    	}
    	return _d;
    }
    
    public static void  main(String[] arg){
    	String s1 = "/";
    	String s2 = "/mysql/";
    	String s3 = "/mysql/test/";
    	String s4 = "/mysql/test/tun.ftl";
    	String pattern = "^"+s2+"[A-Za-z0-9_]+/$";
    	String ftl = "^"+s3+"([A-Za-z0-9_]+).ftl$";
    	// 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(s3);
        
        System.out.println(matcher.find());
        
        Pattern r2 = Pattern.compile(ftl);
        Matcher matcher3 = r2.matcher(s4);
        
        System.out.println(matcher3.find());
        
        
        
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
            /*for(int i=0;i<s.size();i++){
                System.out.println(s.get(i));
            }*/
            String r5 = JSONUtils.toJSONString(s);
            System.out.println(r5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package tech.yobbo.engine.support.http;

import tech.yobbo.engine.support.data.EngineDataManagerFacade;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Map;

/**
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

	// 获取模板中数据
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
			Method m = ctx.getClass().getMethod("getBean", Class.class);
			dataSource = (DataSource) m.invoke(ctx
					, Class.forName(EngineViewServlet.getDataSource()));
            dataSource_className = EngineViewServlet.getDataSource();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void  main(String[] arg){
    	/*String s1 = "/";
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
        
        System.out.println(matcher3.find());*/

        /*String prefix = EngineViewServlet.RESOURCE_PATH + "/template";
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
            *//*for(int i=0;i<s.size();i++){
                System.out.println(s.get(i));
            }*//*
            String r5 = JSONUtils.toJSONString(s);
            System.out.println(r5);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public DataSource getDataSource() {
        return dataSource;
    }
    public String getDataSource_className() {
        return dataSource_className;
    }
}

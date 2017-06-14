package tech.yobbo.engine.support.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

import tech.yobbo.engine.support.http.EngineDataService;
import tech.yobbo.engine.support.http.EngineDataServiceHelp;
import tech.yobbo.engine.support.http.EngineViewServlet;
import tech.yobbo.engine.support.json.JSONUtils;
import tech.yobbo.engine.support.util.JdbcUtils;
import tech.yobbo.engine.support.util.Utils;
import tech.yobbo.engine.support.util.VERSION;

/**
 * Created by xiaoJ on 2017/6/13.
 */
public class EngineDataManagerFacade {
    private EngineDataManagerFacade(){}
    private final static EngineDataManagerFacade instance    = new EngineDataManagerFacade();
    public static EngineDataManagerFacade getInstance(){
        return instance;
    }

    /**
     * 获取code中的数据
     * jar_path engine-1.0.0.jar 在磁盘的绝对位置
     * templatePath 为资源文件在engine-1.0.0.jar 的相对位置
     * * @param parameters
     * @return
     */
    public Map getCodeInfo(Map<String, String> parameters) {
        String jar_path = parameters.get("jar_path") !=null ? parameters.get("jar_path") : "";
        String templatePath = parameters.get("templatePath") != null ? parameters.get("templatePath") : "";
        Map<String,Object> data = new HashMap<String, Object>();
        if(!"".equals(jar_path)){
            try {
                String prefix = EngineViewServlet.getResourcePath() + "/template";
                JarFile jar = new JarFile(jar_path);
                ZipEntry entry =  jar.getEntry(prefix+templatePath);
                InputStream in = jar.getInputStream(entry);
                String text = Utils.read(in);
                data.put("code",text);
                data.put("readOnly",parameters.get("readOnly"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 获取首页基础数据
     */
    public Map getBasicInfo(Map<String,String> params){
        Map<String,Object> dataMap = new LinkedHashMap();
        dataMap.put("Version", VERSION.getVersionNumber());
        dataMap.put("template_path", "file:/D:/engineJar/engine-1.0.0.jar!/engine/http/resources/template");
//  jar       file:/E:/公司项目源码/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/das/WEB-INF/lib/engine-1.0.0.jar!/engine/http/resources/template
//  类        /E:/电影网站模板/FrontMusik/FrontMusikEngine/target/FrontMusik-Engine/WEB-INF/classes/engine/http/resources/template/
//        dataMap.put("template_path", Thread.currentThread().getContextClassLoader().getResource(EngineViewServlet.getResourcePath() +"/template").getPath());
//  jar       jar:file:/E:/公司项目源码/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/das/WEB-INF/lib/engine-1.0.0.jar!/engine/http/resources/template
//  类        file:/E:/电影网站模板/FrontMusik/FrontMusikEngine/target/FrontMusik-Engine/WEB-INF/classes/engine/http/resources/template/

        dataMap.put("common_path", "");
        dataMap.put("treeUrl","tree.json?template_path="+dataMap.get("template_path"));
        dataMap.put("base_path",params.get("base_path"));
        dataMap.put("package_name",params.get("package_name"));
        dataMap.put("dataSource", EngineDataService.getInstance().getDataSource_className());
        dataMap.put("Drivers", params.get("dataSource"));
        dataMap.put("JavaVMName", System.getProperty("java.vm.name"));
        dataMap.put("JavaVersion", System.getProperty("java.version"));
        dataMap.put("StartTime", Utils.getStartTime());
        if(EngineDataService.getInstance().getDataSource() != null){
            dataMap.put("historyData",this.getIndexList());
        }
        return dataMap;
    }

    /**
     * 获取模板中树形菜单
     * @param parameters
     * @return
     */
    public String getTemplateTree(Map<String, String> parameters) {
        String jar_path = parameters.get("template_path");
        String prefix = EngineViewServlet.getResourcePath() + "/template";

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
            String firstTemplate = "";
            while(enums.hasMoreElements()){
                JarEntry entry = (JarEntry) enums.nextElement();
                if(entry.getName().startsWith(prefix)){
                    data.add(entry.getName().replaceAll(prefix,""));
                    if("".equals(firstTemplate) && entry.getName().endsWith(".ftl")){
                        firstTemplate = entry.getName().replaceAll(prefix,"");
                    }
                }
            }
            List list = recursionDirectory(data, "/");
            StringBuilder _data = new StringBuilder();
            _data.append("{\"tree\":").append(JSONUtils.toJSONString(list))
                    .append(",\"firstTemplate\":\"").append(firstTemplate)
                    .append("\",\"jar_path\":\"").append(jar_path)
                    .append("\"}");
            return _data.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过递归，组织模板左边树形菜单
     * 其中运动正则表达式来筛选
     * @param data 数据
     * @param start 筛选起点位置
     * @return
     */
    private static List<Map<String,Object>> recursionDirectory(List<String> data,String start){
        List<Map<String,Object>> _d = new ArrayList<Map<String,Object>>();
        for(int i=1;i<data.size();i++){
            String pattern = "^"+start+"[A-Za-z0-9_]+/$";
            String ftlPattern = "^"+start+"([A-Za-z0-9_]+).ftl$";
            Pattern r = Pattern.compile(pattern);
            Pattern ftl_r = Pattern.compile(ftlPattern);
            Map<String,Object> map = new HashMap<String, Object>();
            if(r.matcher(data.get(i)).find()){
                map.put("name", data.get(i).replaceFirst(start,"").replace("/",""));
                List<Map<String,Object>> _d_child = recursionDirectory(data,data.get(i));
                if(_d_child !=null && _d_child.size() > 0){
                    map.put("children",_d_child);
                }
                _d.add(map);
            }else if(ftl_r.matcher(data.get(i)).find()){
                map.put("name", data.get(i).replaceFirst(start,""));
                map.put("params",data.get(i));
                _d.add(map);
//                return _d;
            }
        }
        return _d;
    }

    /**
     *  获取首页列表
     */
    public List getIndexList() {
        try {
            JdbcUtils jdbcUtils = JdbcUtils.getInstance();
            jdbcUtils.getConnection(EngineDataService.getInstance().getDataSource());
            List data = jdbcUtils.getDataBySql(EngineDataServiceHelp.INDEX_SQL,new Object[]{0,50});
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
    	//读取内容
    	String prefix = EngineViewServlet.getResourcePath() + "/template";
        String path = "file:/D:/engineJar/engine-1.0.0.jar!/engine/http/resources/template";
        if (path.startsWith("file:") && path.indexOf("file:") != -1) {
            path = path.substring(5,path.length());
        }
        if (path.indexOf(".jar") != -1) {
            path = path.substring(0,path.indexOf(".jar")+4);
        }
        try {
			JarFile jar = new JarFile(path);
			ZipEntry entry =  jar.getEntry("engine/http/resources/template/mysql/hibernate/service.ftl");
			InputStream in = jar.getInputStream(entry);
			InputStreamReader reader = new InputStreamReader(in, "utf-8");
			StringWriter stringW = new StringWriter();
			char[] b = new char[1024];
			int i = 0;
			while((i = reader.read(b)) != -1){
				stringW.write(b, 0, i);
			}
			String text = stringW.toString();
			System.out.println(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}
}

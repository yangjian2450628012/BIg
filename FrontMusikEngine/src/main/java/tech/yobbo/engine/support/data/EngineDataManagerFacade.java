package tech.yobbo.engine.support.data;

import tech.yobbo.engine.support.http.EngineDataService;
import tech.yobbo.engine.support.http.EngineDataServiceHelp;
import tech.yobbo.engine.support.http.EngineViewServlet;
import tech.yobbo.engine.support.json.JSONUtils;
import tech.yobbo.engine.support.util.JdbcUtils;
import tech.yobbo.engine.support.util.Utils;
import tech.yobbo.engine.support.util.VERSION;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

/**
 * engine业务功能实现类
 * Created by xiaoJ on 2017/6/13.
 */
public class EngineDataManagerFacade {
    private String jar_path                                     = null;
    private EngineDataManagerFacade(){}
    private final static EngineDataManagerFacade instance       = new EngineDataManagerFacade();
    public static EngineDataManagerFacade getInstance(){
        return instance;
    }

    /**
     * 获取code中的数据
     * 需要参数: 
     * 		1) templatePath --> 比如 ：templatePath=/oracle/hibernate/entity.ftl，对应ftl在模板中的位置
     * 		2) prefix--> 比如：prefix=engine/http/resources/template，模板在 jar包中的位置
     * * @param parameters
     * @return
     */
    public Map getCodeInfo(Map<String, String> parameters) {
        //String jar_path = parameters.get("jar_path") !=null ? parameters.get("jar_path") : "";
        String prefix = parameters.get("prefix") != null ? parameters.get("prefix") : "";
        Map<String,Object> data = new HashMap<String, Object>();
        if(!"".equals(jar_path) && !"".equals(prefix)){
            try {
                String templatePath = parameters.get("templatePath") != null ? parameters.get("templatePath") : "";
                JarFile jar = new JarFile(jar_path);
                ZipEntry entry =  jar.getEntry(prefix+templatePath);
                InputStream in = jar.getInputStream(entry);
                String text = Utils.read(in);
                data.put("code",text);
                data.put("readOnly",parameters.get("readOnly"));
                jar.close();
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
        Map<String,Object> dataMap = new LinkedHashMap<String,Object>();
        dataMap.put("Version", VERSION.getVersionNumber());
        dataMap.put("template_path", "file:/D:/engineJar/engine-1.0.0.jar!/engine/http/resources/template");
//  jar       file:/E:/公司项目源码/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/das/WEB-INF/lib/engine-1.0.0.jar!/engine/http/resources/template
//  类        /E:/电影网站模板/FrontMusik/FrontMusikEngine/target/FrontMusik-Engine/WEB-INF/classes/engine/http/resources/template/
//        dataMap.put("template_path", Thread.currentThread().getContextClassLoader().getResource(EngineViewServlet.getResourcePath() +"/template").getPath());
//  jar       jar:file:/E:/公司项目源码/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/das/WEB-INF/lib/engine-1.0.0.jar!/engine/http/resources/template
//  类        file:/E:/电影网站模板/FrontMusik/FrontMusikEngine/target/FrontMusik-Engine/WEB-INF/classes/engine/http/resources/template/

        dataMap.put("common_path", "");
        dataMap.put("treeUrl","tree.json?prefix="+EngineViewServlet.getResourcePath()+"/template");
        dataMap.put("base_path",params.get("base_path"));
        dataMap.put("package_name",params.get("package_name"));
        dataMap.put("dataSource", EngineDataService.getInstance().getDataSource_className());
        dataMap.put("Drivers", params.get("dataSource"));
        dataMap.put("JavaVMName", System.getProperty("java.vm.name"));
        dataMap.put("JavaVersion", System.getProperty("java.version"));
        dataMap.put("StartTime", EngineDataService.getInstance().getStartTime());
        if(EngineDataService.getInstance().getDataSource() != null){
            dataMap.put("historyData",this.getIndexList());
        }
        jar_path = dataMap.get("template_path").toString();
        if (jar_path.startsWith("file:") && jar_path.indexOf("file:") != -1) {
            jar_path = jar_path.substring(5,jar_path.length());
        }
        if (jar_path.indexOf(".jar") != -1) {
            jar_path = jar_path.substring(0,jar_path.indexOf(".jar")+4);
        }
        return dataMap;
    }

    /**
     * 获取模板中树形菜单
     * 需要参数:
     *  1) prefix-->比如：prefix=engine/http/resources/template
     * @param parameters
     * @return
     */
    public String getTemplateTree(Map<String, String> parameters) {
        String prefix = parameters.get("prefix");
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
            List list = recursionDirectory(data, "/",prefix);
            StringBuilder _data = new StringBuilder();
            _data.append("{\"tree\":").append(JSONUtils.toJSONString(list))
                    .append(",\"firstTemplate\":\"").append(firstTemplate)
                    //.append("\",\"jar_path\":\"").append(jar_path)
                    .append("\",\"prefix\":\"").append(prefix)
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
    private static List<Map<String,Object>> recursionDirectory(List<String> data,String start,String prefix){
        List<Map<String,Object>> _d = new ArrayList<Map<String,Object>>();
        for(int i=1;i<data.size();i++){
            String pattern = "^"+start+"[A-Za-z0-9_]+/$";
            String ftlPattern = "^"+start+"([A-Za-z0-9_]+).ftl$";
            Pattern r = Pattern.compile(pattern);
            Pattern ftl_r = Pattern.compile(ftlPattern);
            Map<String,Object> map = new HashMap<String, Object>();
            if(r.matcher(data.get(i)).find()){
                map.put("name", data.get(i).replaceFirst(start,"").replace("/",""));
                List<Map<String,Object>> _d_child = recursionDirectory(data,data.get(i),prefix);
                if(_d_child !=null && _d_child.size() > 0){
                    map.put("children",_d_child);
                }
                _d.add(map);
            }else if(ftl_r.matcher(data.get(i)).find()){
                map.put("name", data.get(i).replaceFirst(start,""));
                map.put("params",data.get(i));
                map.put("prefix",prefix);
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
        JdbcUtils jdbcUtils = JdbcUtils.getInstance();
        try {
            jdbcUtils.getConnection(EngineDataService.getInstance().getDataSource());
            List<Map<String,Object>> data = jdbcUtils.getDataBySql(EngineDataServiceHelp.INDEX_SQL,new Object[]{0,50});
            for(int i=0;i<data.size();i++){
                Map<String,Object> map = data.get(i);
                String common_template = map.get("COMMON_TEMPLATE").toString();
                map.put("COMMON_TEMPLATE",jar_path+"/"+common_template);
                map.put("treeUrl","tree.json?prefix="+common_template);
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                jdbcUtils.closeDb();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {

	}
}

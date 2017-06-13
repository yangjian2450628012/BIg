package tech.yobbo.engine.support.data;

import tech.yobbo.engine.support.http.EngineDataService;
import tech.yobbo.engine.support.http.EngineViewServlet;
import tech.yobbo.engine.support.json.JSONUtils;
import tech.yobbo.engine.support.util.JdbcUtils;
import tech.yobbo.engine.support.util.Utils;
import tech.yobbo.engine.support.util.VERSION;

import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import static tech.yobbo.engine.support.http.EngineDataServiceHelp.INDEX_SQL;

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
     * @param parameters
     * @return
     */
    public Map getCodeInfo(Map<String, String> parameters) {
        // TODO 后期完善
        return parameters;
    }

    /**
     * 获取首页基础数据
     */
    public Map getBasicInfo(Map<String,String> params){
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
        dataMap.put("dataSource", EngineDataService.getInstance().getDataSource_className());
        dataMap.put("Drivers", params.get("dataSource"));
        dataMap.put("JavaVMName", System.getProperty("java.vm.name"));
        dataMap.put("JavaVersion", System.getProperty("java.version"));
        dataMap.put("StartTime", Utils.getStartTime());
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
                return _d;
            }
        }
        return _d;
    }

    /**
     *  获取首页列表
     */
    public List getIndexList(Map<String, String> parameters) {
        try {
            JdbcUtils jdbcUtils = JdbcUtils.getInstance();
            jdbcUtils.getConnection(EngineDataService.getInstance().getDataSource());
            List data = jdbcUtils.getDataBySql(INDEX_SQL,new Object[]{0,100000});
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
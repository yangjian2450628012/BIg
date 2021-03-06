package tech.yobbo.engine.support.data;

import tech.yobbo.engine.support.http.EngineDataService;
import tech.yobbo.engine.support.http.EngineDataServiceHelp;
import tech.yobbo.engine.support.http.EngineViewServlet;
import tech.yobbo.engine.support.json.JSONUtils;
import tech.yobbo.engine.support.util.JdbcUtils;
import tech.yobbo.engine.support.util.Utils;
import tech.yobbo.engine.support.util.VERSION;

import java.io.*;
import java.net.URLDecoder;
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
    private EngineDataManagerFacade(){}
    private final static EngineDataManagerFacade instance       = new EngineDataManagerFacade();
    public static EngineDataManagerFacade getInstance(){
        return instance;
    }

    /**
     * 获取code中的数据
     * 需要参数: 
     * 		1) templatePath >> 比如 ：templatePath=/oracle/hibernate/entity.ftl，对应ftl在模板中的位置
     * 		2) prefix >> 比如：prefix=engine/http/resources/template，模板在 jar包中的位置
     * 	    3) 不是模板情况 java_base_path >> 比如：E:/电影网站模板/FrontMusik/FrontMusikEngine/src/main/java/tech/yobbo/index/service/IndexService.java
     * * @param parameters
     * @return
     */
    public Map getCodeInfo(Map<String, String> parameters) {
        Map<String,Object> data = new HashMap<String, Object>();
        if(parameters.containsKey("prefix") && parameters.containsKey("templatePath")){
            String prefix = parameters.get("prefix") != null ? parameters.get("prefix") : "";
            if(!"".equals(EngineDataService.getInstance().getJar_path()) && !"".equals(prefix)){
                try {
                    String templatePath = parameters.get("templatePath") != null ? parameters.get("templatePath") : "";
                    JarFile jar = new JarFile(EngineDataService.getInstance().getJar_path());
                    ZipEntry entry =  jar.getEntry(prefix+templatePath);
                    InputStream in = jar.getInputStream(entry);
                    String text = Utils.read(in);
                    data.put("code",text);
                    data.put("readOnly",parameters.get("readOnly"));
                    in.close();
                    jar.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(parameters.containsKey("java_base_path")){
            String filepath = parameters.get("java_base_path");
            try {
                filepath = URLDecoder.decode(filepath,"utf-8");
                File file = new File(filepath);
                InputStream in = new FileInputStream(file);
                String text = Utils.read(in);
                data.put("code",text);
                data.put("readOnly",parameters.get("readOnly"));
                in.close();
            } catch (Exception e) {
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
        dataMap.put("template_path", EngineDataService.getInstance().getJar_path()+"/engine/http/resources/template");
        dataMap.put("common_path", "");
        dataMap.put("treeUrl","tree.json?prefix="+EngineViewServlet.getResourcePath()+"/template");
        dataMap.put("base_path",params.get("base_path"));
        dataMap.put("package_name",params.get("package_name"));
        dataMap.put("dataSource", EngineDataService.getInstance().getDataSource_className());
        dataMap.put("Drivers", params.get("dataSource"));
//        dataMap.put("JavaVMName", System.getProperty("java.vm.name"));
        dataMap.put("db_version",EngineDataService.getInstance().getDb_version());
        dataMap.put("JavaVersion", System.getProperty("java.version"));
        dataMap.put("StartTime", EngineDataService.getInstance().getStartTime()!=null ? EngineDataService.getInstance().getStartTime() : new Date());
        if(EngineDataService.getInstance().getDataSource() != null){
            dataMap.put("historyData",this.getIndexList());
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
            JarFile jar = new JarFile(EngineDataService.getInstance().getJar_path());
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
                    .append(",\"firstTemplate\":\"").append(firstTemplate).append("\"")
                    .append(",\"prefix\":\"").append(prefix).append("\"")
                    .append("}");
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
     * 获取java代码中的树形菜单结构
     * 需要参数：
     * 1) path --> E:/电影网站模板/FrontMusik/FrontMusikEngine/src/main/java
     * @param parameters
     * @return
     */
    public String getJavaBaseTree(final Map<String, String> parameters) {
        String path = parameters.get("path");
        try {
            path = URLDecoder.decode(path,"utf-8");
        } catch (UnsupportedEncodingException e) {}
        if(!parameters.containsKey("path") || path == null) return null;

        File file = new File(path);
        if(!file.exists()) return null;

        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File file) {
                StringBuilder ex = new StringBuilder();
                ex.append(EngineViewServlet.getPackage_name()!=null?EngineViewServlet.getPackage_name().replaceAll("\\.","|")+"|":"")
                        .append(parameters.get("module"))
                        .append("|web|service|dao|pojo|([A-Za-z]+.java)");
                if(file.getName().matches(ex.toString())){
                    return true;
                }
                return false;
            }
        };
        List<String> files = new ArrayList<String>();
        files.add(path+"/");
        String firstJavaBase = "";
        EngineDataManagerFacade.getInstance().getDirList(file,fileFilter,files);
        for(int i=0;i<files.size();i++){
            if(files.get(i).startsWith(path)){
                if("".equals(firstJavaBase) && files.get(i).endsWith(".java")){
                    firstJavaBase = files.get(i);
                }
                files.set(i,files.get(i).replaceAll(path,""));
            }
        }
        List list = recursionJavaBaseDirectory(files,"/",path);
        StringBuilder v = new StringBuilder();
        v.append("{\"tree\":").append(JSONUtils.toJSONString(list))
                .append(",\"firstJavaBase\":\"").append(firstJavaBase).append("\"")
                .append("}");
        return v.toString();
    }

    /**
     * 递归遍历java代码树形菜单
     * @param files
     * @param start
     * @param prefix
     * @return
     */
    private static List<Map<String,Object>> recursionJavaBaseDirectory(List<String> files,String start,String prefix){
        List<Map<String,Object>> _d = new ArrayList<Map<String,Object>>();
        for(int i=1;i<files.size();i++){
            String pattern = "^"+start+"[A-Za-z0-9_]+$";
            String ftlPattern = "^"+start+"([A-Za-z0-9_]+).java$";
            Pattern r = Pattern.compile(pattern);
            Pattern ftl_r = Pattern.compile(ftlPattern);
            Map<String,Object> map = new HashMap<String, Object>();
            if(r.matcher(files.get(i)).find()){
                map.put("name", files.get(i).replaceFirst(start,"").replace("/",""));
                List<Map<String,Object>> _d_child = recursionJavaBaseDirectory(files,files.get(i)+"/",prefix);
                if(_d_child !=null && _d_child.size() > 0){
                    map.put("children",_d_child);
                }
                _d.add(map);
            }else if(ftl_r.matcher(files.get(i)).find()){
                map.put("name", files.get(i).replaceFirst(start,""));
                map.put("path",prefix+files.get(i));
                _d.add(map);
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
                String java_base = map.get("BASE_PATH").toString();
                map.put("COMMON_TEMPLATE",EngineDataService.getInstance().getJar_path()+"/"+common_template);
                map.put("treeUrl","tree.json?prefix="+common_template);
                map.put("treeUrl_javaBase","treeJavaBase.json?path="+java_base+"&module="+map.get("MODULE"));
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

	// 递归获取目录和文件
	private void getDirList(File file,FileFilter fileFilter,List<String> files){
        File[] f = file.listFiles(fileFilter);
        if(f != null && f.length >= 1){
            for(int i=0;i<f.length;i++){
                files.add(f[i].getPath().replaceAll("\\\\","/"));
                this.getDirList(f[i],fileFilter,files);
            }
        }
    }

    public static void main(String[] args) {

    }
}

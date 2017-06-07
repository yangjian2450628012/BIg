package tech.yobbo.engine.support.http;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import tech.yobbo.engine.support.util.JdbcUtils;
import tech.yobbo.engine.support.util.Utils;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

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

    /**
     * 调用服务
     * @param url
     * @param context 上下文
     * @return
     */
    public static String process(String url,ServletContext context){
        Map<String, String> parameters = getParameters(url);
        // 获取spring中的连接池
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        try {
            dataSource = (DataSource) ctx.getBean(Class.forName(EngineViewServlet.getDataSource()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (url.startsWith(INDEX_URL)) {
            return returnJSONResult(RESULT_CODE_SUCCESS, getIndexList(parameters));
        }
        if (url.startsWith(INDEX_CHANGE_PATH)) {
            return returnJSONResult(RESULT_CODE_SUCCESS, changePath(parameters));
        }
        return returnJSONResult(RESULT_CODE_ERROR, "Do not support this request, please contact with administrator.");
    }

    // 修改路径
    private static Object changePath(Map<String, String> parameters) {
        try{
            // 判断数据表是否已经创建
            String sqlMsql = Utils.readFromResource(DB_SUPPORT+"/mysql_base.sql");
            JdbcUtils jdbcUtils = JdbcUtils.getInstance();
            jdbcUtils.getConnection(dataSource);
            boolean r = jdbcUtils.execute(sqlMsql);
            if (!r){
                String oracleSQl = Utils.readFromResource(DB_SUPPORT + "/oracle_base.sql");
                jdbcUtils.execute(oracleSQl);
            }
            // 保存信息

        } catch(Exception e){
            e.printStackTrace();
        }


        return null;
    }

    // 获取首页列表
    private static List getIndexList(Map<String, String> parameters) {
        JdbcUtils jdbcUtils = JdbcUtils.getInstance();
        try {
            List data = jdbcUtils.getDataBySql(INDEX_SQL,new Object[]{0,100000});
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

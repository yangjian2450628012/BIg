package tech.yobbo.engine.support.util;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaoJ on 6/1/2017.
 * 数据库操作公共类
 */
public class JdbcUtils {
//    private static final Properties DRIVER_URL_MAPPING  = new Properties();
    private static Connection conn                      = null;
    private static PreparedStatement statement          = null;
    private static ResultSet resultSet                  = null;
    private JdbcUtils(){}
    private static JdbcUtils instance                   = null;
    public static JdbcUtils getInstance(){
        if (instance == null) {
            instance = new JdbcUtils();
        }
        return instance;
    }

    // 获取连接
    public void getConnection(DataSource dataSource) throws Exception {
        if (dataSource == null) throw  new Exception("没有连接池");
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 关闭数据库
    public static void closeDb() throws SQLException {
        if (conn != null) {
            conn.close();
        }
        if (statement != null) {
            statement.close();
        }
        if (resultSet != null) {
            resultSet.close();
        }
    }

    /********************start 执行提交**************************************************/

    // 创建表
    public  boolean execute(String sql){
        if (conn == null) {
           throw new RuntimeException("没有可用的连接");
        }
        try {
            statement = conn.prepareStatement(sql);
            return statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 执行提交
    public Integer execetuUPdate (String sql,Object... params) throws RuntimeException {
        if (conn == null) {
            throw new RuntimeException("没有可用的连接");
        }
        try {
            statement = conn.prepareStatement(sql,Connection.TRANSACTION_READ_UNCOMMITTED);
            if (params != null && params.length > 0) {
                for (int i=0;i<params.length;i++) {
                    statement.setObject(i+1,params[i]);
                }
            }
            int count =statement.executeUpdate();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                closeDb();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    /********************end 执行提交****************************************************/

    /*******************start sql查询方法************************************************/
    // 获取查询结果
    private static ResultSet queryData(String sql,Object ... params) throws Exception {
        if (null == conn) {
            throw new Exception("没有可用的连接");
        }
        statement = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        if (params != null && params.length > 0) {
            for (int i=0;i<params.length;i++) {
                statement.setObject(i+1,params[i]);
            }
        }
        resultSet = statement.executeQuery();
        return resultSet;
    }

    // 通过sql查询数据
    public List getDataBySql(String sql,Object... params) throws Exception {
        if (null == conn) {
            throw new Exception("没有可用的连接");
        }
        List data = new ArrayList();
        try {
            resultSet = queryData(sql, params);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount(); // 返回此 ResultSet 对象中的列数
            while (resultSet.next()) {
                Map rowData = new HashMap(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    String dbTypeName = metaData.getColumnTypeName(i);
                    String oValue = "";
                    if ("VARCHAR2".equals(dbTypeName)) {
                        oValue = null == resultSet.getObject(i) ? "" : resultSet.getObject(i).toString();
                    } else if ("VARCHAR ".equals(dbTypeName)) {
                        oValue = null == resultSet.getObject(i) ? "" : resultSet.getObject(i).toString();
                    } else if ("NUMBER".equals(dbTypeName)) {
                        oValue = null == resultSet.getObject(i) ? "" : String.valueOf(resultSet.getObject(i));
                    } else {
                        // 其他类型的未处理,先强制转字符串
                        oValue = null == resultSet.getObject(i) ? "" : String.valueOf(resultSet.getObject(i));
                    }
                    rowData.put(metaData.getColumnName(i).toUpperCase(), oValue);
                }
                data.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDb();
        }
        return data;
    }
    /*****************************end sql查询方法***************************************/

//    static {
//        try {
//            ClassLoader ctxClassLoader = Thread.currentThread().getContextClassLoader();
//            if (ctxClassLoader != null) {
//                for (Enumeration<URL> e = ctxClassLoader.getResources("META-INF/engine-driver.properties"); e.hasMoreElements();) {
//                    URL url = e.nextElement();
//
//                    Properties property = new Properties();
//
//                    InputStream is = null;
//                    try {
//                        is = url.openStream();
//                        property.load(is);
//                    } finally {
//                        JdbcUtils.close(is);
//                    }
//
//                    DRIVER_URL_MAPPING.putAll(property);
//                }
//            }
//        } catch (Exception e) {
//           e.printStackTrace();
//        }
//    }



    /**
     * 关闭数据库流
     * @param x
     */
    public static void close(Statement x){
        if (x == null) {
            return;
        }
        try {
            x.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 关闭文件流
     * @param x
     */
    public static void close(Closeable x){
        if (x == null) {
            return;
        }
        try {
            x.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

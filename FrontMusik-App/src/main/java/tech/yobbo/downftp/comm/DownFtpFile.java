package tech.yobbo.downftp.comm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;


/**
 * 查询FTP电影url,查询下载完成一部，再查询一部
 * @author xiaoyang
 *
 */
public class DownFtpFile {
    private static String url = "jdbc:mysql://xiaoyang.mysql.rds.aliyuncs.com:3306/xiaoyang_movie";
    private static String uid = "xiaoyang_666";
    private static String pwd = "Yj04051711_666";
    private static int start = 1;

    public static void main(String[] args) {
        if(args.length >= 4){
            url = args[0]; uid = args[1]; pwd = args[2] ;start = Integer.valueOf(args[3]); //从第几个开始查询
        }
        DbHelp dbHelp = new DownFtpFile().new DbHelp(url,uid,pwd);
        while(true){
            System.out.println("start :"+start);
            try {
                dbHelp.getCon();
                String sql = "SELECT m.movie_name,m.translation,m.url FROM `movie_detail_splider` m "
                        + "where  m.success_status ='' limit "+(start - 1)+",1";
                Map<String,String> map = dbHelp.queryData(sql);
                if(map != null){
                    String ftpUrl = map.get("url"),movie_name = map.get("movie_name"),translation = map.get("translation");
                    String[] uid_pwd = ftpUrl.substring(ftpUrl.indexOf("//")+2, ftpUrl.indexOf("@")).split(":");
                    String ftp_uid = uid_pwd[0],ftp_pwd = uid_pwd[1];
                    String [] host_port = ftpUrl.substring(ftpUrl.indexOf("@")+1, ftpUrl.lastIndexOf("/")).split(":");
                    String ftp_host = host_port[0];int ftp_port = Integer.valueOf(host_port[1]);
                    String filename = ftpUrl.substring(ftpUrl.lastIndexOf("/")+1, ftpUrl.length()-1);
                    System.out.println(map.get("url"));
                    System.out.println(ftp_host+":"+ftp_port+"===>"+ftp_uid+":"+ftp_pwd);
                    FtpUtil ftpUitl = new DownFtpFile().new FtpUtil(ftp_host,ftp_port,ftp_uid,ftp_pwd,filename);
//                     FtpUtil ftpUitl = new DownFtpFile().new FtpUtil("g.dygod18.com",7978,"j","j","金色梦乡BD日语中字[电影天堂www.dy2018.com].mp4");
                    try {
                        if(ftpUitl.getConnect()){ //获取连接
                            String path = ftpUitl.getPath(); //获取指定文件的目录
                            System.out.println("返回的path为:"+path);
                            if(!"".equals(path) && path != null){
                                System.out.println("path路径为:" + path);
                                return;
                            }else{
                                start ++;
                            }
                        }else{
                            start ++;
                        }
                    } catch (Exception e) {
                        start ++;
                        e.printStackTrace();
                    }
                }else{
                    start ++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class FtpUtil{
        private String host;
        private Integer port;
        private String uid;
        private String pwd;
        private String filename;
        private FTPClient ftpClient;
        private String path;
        public FtpUtil(String host, int port, String uid, String pwd, String filename)
                throws IOException {
            this.host = host;
            this.port = port;
            this.uid = uid;
            this.pwd = pwd;
            this.filename = filename;
        }
        /**
         * 获取连接
         * @return
         * @throws IOException
         */
        public boolean getConnect()
                throws IOException{
            ftpClient = new FTPClient();
            ftpClient.connect(host, port);
            ftpClient.login(uid, pwd);
            ftpClient.setConnectTimeout(1000); // 一秒钟，如果超过就判定超时了
			ftpClient.setControlEncoding("GBK");
            FTPClientConfig conf = new FTPClientConfig( FTPClientConfig.SYST_UNIX); //设置linux环境
            conf.setServerLanguageCode("zh");
            ftpClient.configure(conf);
            ftpClient.enterLocalPassiveMode(); //设置被动模式
            int reply = ftpClient.getReplyCode(); //判断是否连接成功
            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftpClient.disconnect();
                return false;
            }
            return true;
        }

        /**
         * 递归遍历出目录下面所有文件
         * @param pathName 需要遍历的目录，必须以"/"开始和结束
         * @throws IOException
         */
        public void getFtpPath(String pathName,String filename) throws IOException{
            boolean r = ftpClient.changeWorkingDirectory(pathName); //切换目录
            if(r){
                FTPFile[] files = ftpClient.listFiles();
                for (int i = 0; i < files.length; i++) {
                    FTPFile file = files[i];
                    System.out.println(file.getName());
                    if(file.isFile()){
                        if(filename.equals(file.getName())) {
                            this.path = pathName + file.getName();
                            return;
                        }
                    }else if(file.isDirectory() ){
                        if(".".equals(file.getName()) || "..".equals(file.getName())){
                            continue;
                        }else{
                            this.getFtpPath(pathName + file.getName() +"/", filename);
                        }
                    }
                }
            }
        }

        private void disConn() throws IOException{
            if(ftpClient != null){
                ftpClient.disconnect();
            }
        }

        /**
         * 获取文件在ftp中的位置
         * @return
         * @throws IOException
         */
        public String getPath() throws IOException{
            getFtpPath("/",this.filename);
            this.disConn(); //关闭连接
            return this.path;
        }
    }

    /**
     * 数据库操作类
     * @author xiaoyang
     *
     */
    class DbHelp{
        private Connection con;
        private Statement stmt;
        private ResultSet rs;
        private String url;
        private String uid;
        private String pwd;
        public DbHelp(String url, String uid, String pwd) {
            this.url = url;
            this.uid = uid;
            this.pwd = pwd;
        }
        /***
         * 得到数据库连接
         * <p>
         * 一个操作中只能打开一次连接（一个操作包括要执行好几个sql）
         *
         * @return
         * @throws Exception
         */
        public void getCon() throws Exception {
            if (null == con) {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(url, uid, pwd);
            }
        }

        /***
         * 查询数据库中的数据，执行select语句 使用完后请手工关闭Statement和ResultSet
         * <p>
         * 调用stmtClose()方法
         *
         * @param sql
         * @return
         * @throws Exception
         */
        public Map<String,String> queryData(String sql) throws Exception {
            this.getCon();
            if (null == con) {
                throw new Exception("没有可用的连接");
            }
            if ("".equals(sql) || sql == null) {
                throw new Exception("sql语句无效");
            }
            Map<String,String> map = null;
            this.stmt = this.con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);// 创建语句对象
            this.rs = this.stmt.executeQuery(sql);
            if(rs.next()){
                map = new HashMap<String, String>();
                map.put("movie_name", rs.getString(1));
                map.put("translation", rs.getString(2));
                map.put("url", rs.getString(3));
            }
            dbClose();
            stmtClose();
            return map;
        }

        /***
         * 关闭数据库操作的所有资源(全部操作执行完后再调用)，此方法将关闭数据库连接，请慎用
         */
        private void dbClose() throws Exception {
            if (null != this.rs) {
                this.rs.close();
                this.rs = null;
            }
            if (null != this.stmt) {
                this.stmt.close();
                this.stmt = null;
            }
            if (null != this.con) {
                this.con.close();
                this.con = null;
            }
        }

        /**
         * 关闭并释放Statement和ResultSet
         *
         * @throws Exception
         */
        private void stmtClose() throws Exception {
            if (null != this.rs) {
                this.rs.close();
                this.rs = null;
            }
            if (null != this.stmt) {
                this.stmt.close();
                this.stmt = null;
            }
        }
    }

}

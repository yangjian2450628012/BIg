package tech.yobbo.engine.support.http;


import tech.yobbo.engine.support.json.JSONUtils;
import tech.yobbo.engine.support.util.JdbcUtils;
import tech.yobbo.engine.support.util.Utils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by xiaoJ on 5/31/2017.
 * 自动化引擎-自动生成JAVA代码类
 */
public class EngineViewServlet extends HttpServlet{
    private static final Logger LOG = Logger.getLogger(EngineViewServlet.class.getName());

    private String resourcePath                    = "engine/http/resources";
    private static String dataSource;
    public static String getDataSource() {
        return dataSource;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        dataSource = config.getInitParameter("dataSource_class");
        LOG.log(Level.INFO,"engineViewServlet初始化成功!");
//        super.init(config);
    }

    /**
     * 获取jar包中的文件名
     * @param fileName 文件名
     * @return
     */
    protected String getFilePath(String fileName) {
        return resourcePath + fileName;
    }

    protected void returnResourceFile(String fileName, String uri, HttpServletResponse response)
            throws ServletException, IOException
    {
        String filePath = getFilePath(fileName);

        if (filePath.endsWith(".html")) {
            response.setContentType("text/html; charset=utf-8");
        }

        if (fileName.endsWith(".jpg")
                || fileName.endsWith(".png")
                || fileName.endsWith(".gif")) {
            byte[] bytes = Utils.readByteArrayFromResource(filePath);
            if (bytes != null) {
                response.getOutputStream().write(bytes);
            }
            return;
        }

        String text = Utils.readFromResource(filePath);
        if (text == null) {
            response.sendRedirect(uri + "/index.html");
            return;
        }
        if (fileName.endsWith(".css")) {
            response.setContentType("text/css;charset=utf-8");
        } else if (fileName.endsWith(".js")) {
            response.setContentType("text/javascript;charset=utf-8");
        }else if (fileName.endsWith(".woff")) {
            response.setContentType("application/x-font-woff");
            response.setBufferSize(402800);
        } else if (fileName.endsWith(".ttf")) {
            response.setHeader("Content-type","application/octet-stream");
            response.setBufferSize(402800);
        } else if (fileName.endsWith(".svg")) {
            response.setHeader("Content-type","mage/svg+xml");
            response.setBufferSize(402800);
        } else if (fileName.endsWith(".eot")) {
            response.setHeader("Content-type","application/vnd.ms-fontobject");
            response.setBufferSize(402800);
        }
        response.getWriter().write(text);
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();

        response.setCharacterEncoding("utf-8");

        if (contextPath == null) { // root context
            contextPath = "";
        }
        String uri = contextPath + servletPath;
        String path = requestURI.substring(contextPath.length() + servletPath.length());

        if ("".equals(path)) {
            if (contextPath.equals("") || contextPath.equals("/")) {
                response.sendRedirect("/engine/index.html");
            } else {
                response.sendRedirect("engine/index.html");
            }
            return;
        }

        if (path.contains(".json")) {
            String fullUrl = path;
            if (request.getQueryString() != null && request.getQueryString().length() > 0) {
                fullUrl += "?" + request.getQueryString();
            }
            response.getWriter().print(EngineDataService.getInstance().process(fullUrl,request.getServletContext()));
            return;
        }

        if ("/".equals(path)) {
            response.sendRedirect("index.html");
            return;
        }

        returnResourceFile(path, uri, response);
    }

}

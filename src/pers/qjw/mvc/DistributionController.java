package pers.qjw.mvc;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pers.qjw.mvc.util.BeanManagement;
import pers.qjw.mvc.util.LoadsObjectsAndMethods;
import pers.qjw.mvc.util.MethodCall;

import java.io.IOException;
import java.util.*;

public class DistributionController extends HttpServlet {

    @Override
    public void init(){
        // 从配置文件中读取 要扫描的包
        String packagePath = super.getInitParameter("packagePath");
        // 加载所有 uri 和 方法 的映射关系
        LoadsObjectsAndMethods loadsObjectsAndMethods = (LoadsObjectsAndMethods) BeanManagement.getBean("loadsObjectsAndMethods");
        if (Objects.isNull(loadsObjectsAndMethods)) {
            BeanManagement.addBean("loadsObjectsAndMethods",new LoadsObjectsAndMethods(packagePath));
            loadsObjectsAndMethods = (LoadsObjectsAndMethods) BeanManagement.getBean("loadsObjectsAndMethods");
        }
        loadsObjectsAndMethods.loadsObjectsAndMethods();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("content-type","text/html;charset=UTF-8");
        // 获取资源地址
        String uri = req.getRequestURI();
        // 获取 资源地址 的请求方式
        String requestWay = req.getMethod();
        // 获取所有参数
        Enumeration<String> names = req.getParameterNames();
        Iterator<String> iterator = names.asIterator();
        List<String> keys = new ArrayList<>();
        while (iterator.hasNext()) {
            keys.add(iterator.next());
        }
        Map<String,String> parameters = new HashMap<>();
        for (String key:
             keys) {
            parameters.put(key,req.getParameter(key));
        }
        // 调用方法 并处理结果
        Object obj = MethodCall.methodCall(uri,requestWay,parameters);
        resp.getWriter().println(obj);
    }

}

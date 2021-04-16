package pers.qjw.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pers.qjw.mvc.bean.UriAndMethodsMapping;
import pers.qjw.mvc.domain.MethodExecutionResources;
import pers.qjw.mvc.util.LoadsObjectsAndMethods;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

public class DistributionController extends HttpServlet {

    @Override
    public void init() throws ServletException {
        // 从配置文件中读取 要扫描的包
        String packagePath = super.getInitParameter("packagePath");

        // 加载所有 uri 和 方法 的映射关系
        LoadsObjectsAndMethods loadsObjectsAndMethods = BeanManagement.getBean("loadsObjectsAndMethods");

        loadsObjectsAndMethods.loadsObjectsAndMethods();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UriAndMethodsMapping uriAndMethodsMapping = BeanManagement.getBean("uriAndMethodsMapping");

        String uri = req.getRequestURI();

        Set<MethodExecutionResources> set = uriAndMethodsMapping.getObjAndMeth(uri);

        String requestWay = req.getMethod();

        for (MethodExecutionResources temp:
             set) {
            System.out.println(temp);
            String methodRequestWay = temp.getRequestWay();
            if (Objects.equals(methodRequestWay,"ALL") || Objects.equals(requestWay,methodRequestWay)) {
                Object obj = temp.getObj();
                Method meth = temp.getMeth();
                try {
                    meth.invoke(obj);
                } catch (IllegalAccessException | InvocationTargetException ignored) {}
            }
        }

    }

}

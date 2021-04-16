package pers.qjw.mvc.util;

import pers.qjw.mvc.bean.UriAndMethodsMapping;
import pers.qjw.mvc.domain.MethodExecutionResources;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MethodCall {

    public static Object methodCall(String uri, String requestWay, Map<String, String> parameters) {
        Object result = null;
        UriAndMethodsMapping uriAndMethodsMapping = (UriAndMethodsMapping) BeanManagement.getBean("uriAndMethodsMapping");
        Set<MethodExecutionResources> set = uriAndMethodsMapping.getMethodExecutionResources(uri);
        // 遍历
        for (MethodExecutionResources temp :
                set) {
            // 筛选出请求方式一致的
            String methodRequestWay = temp.getRequestWay();
            // "ALL" 表示所有请求方式都接受
            if (Objects.equals(methodRequestWay, "ALL") || Objects.equals(requestWay, methodRequestWay)) {
                Object obj = temp.getObj();
                Method meth = temp.getMeth();
                // 判断方法是否有参数(1)
                String[] methodParameters = temp.getParameters();
                Object[] parameterValues;
                if (!Objects.isNull(parameters)) {
                    // 有参数(1)
                    parameterValues = new String[methodParameters.length];
                    for (int i = 0; i < methodParameters.length; i++) {
                        // 获取 请求时携带的参数
                        parameterValues[i] = parameters.get(methodParameters[i]);
                    }
                } else {
                    // 没参数(1)
                    parameterValues = null;
                }
                try {
                    // 判断方法是否有参数(2)
                    if (!Objects.isNull(parameterValues)) {
                        // 有参数(2)
                        // 将请求时携带的参数传给处理请求的方法
                        result = meth.invoke(obj, parameterValues);
                    } else {
                        // 没参数(2)
                        result = meth.invoke(obj);
                    }
                } catch (IllegalAccessException | InvocationTargetException ignored) {}

            }
        }
        return result;
    }

}

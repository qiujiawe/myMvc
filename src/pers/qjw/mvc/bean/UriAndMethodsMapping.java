package pers.qjw.mvc.bean;

import pers.qjw.mvc.domain.MethodExecutionResources;

import java.lang.reflect.Method;
import java.util.*;

public class UriAndMethodsMapping {

    // 用来储存 资源路径 与 方法 的映射关系
    // 执行方法需要 对象 ，所以变成 资源路径 与 方法、对象、请求方式 的映射关系
    Map<String, Set<MethodExecutionResources>> mapping = new HashMap<>();

    public void addMapping(String uri, String requestWay, Method meth, Object obj, String[] parameters) {
        MethodExecutionResources methodExecutionResources = new MethodExecutionResources(obj, meth, requestWay,parameters);
        Set<MethodExecutionResources> set = mapping.get(uri);
        if (Objects.isNull(set)) {
            Set<MethodExecutionResources> temp = new HashSet<>();
            temp.add(methodExecutionResources);
            mapping.put(uri, temp);
        } else {
            set.add(methodExecutionResources);
        }
    }

    public Set<MethodExecutionResources> getMethodExecutionResources(String uri) {
        return mapping.get(uri);
    }

}

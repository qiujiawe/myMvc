package pers.qjw.mvc.domain;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodExecutionResources {

    private final Object obj;
    private final Method meth;
    private final String requestWay;
    private final String[] parameters;

    public MethodExecutionResources(Object obj, Method meth, String requestWay, String[] parameters) {
        this.obj = obj;
        this.meth = meth;
        this.requestWay = requestWay;
        this.parameters = parameters;
    }

    public String[] getParameters(){
        return parameters;
    }

    public String getRequestWay() {
        return requestWay;
    }

    public Object getObj() {
        return obj;
    }

    public Method getMeth() {
        return meth;
    }

    @Override
    public String toString() {
        return "MethodExecutionResources{" +
                "obj=" + obj +
                ", meth=" + meth +
                ", requestWay='" + requestWay + '\'' +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}

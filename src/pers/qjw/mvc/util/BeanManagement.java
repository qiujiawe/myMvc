package pers.qjw.mvc.util;

import java.util.HashMap;
import java.util.Map;

public class BeanManagement {

    private static final Map<String,Object> beans = new HashMap<>();

    public static void addBean(String key,Object value){
        beans.put(key,value);
    }

    public static Object getBean(String name){
        return beans.get(name);
    }

}
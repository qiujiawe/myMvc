package pers.qjw.mvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BeanManagement {

    private static final Map<String,Object> beans = new HashMap<>();

    public static void addBean(String key,Object value){
        beans.put(key,value);
    }

    public static <T>T getBean(String name){
        Object obj = beans.get(name);
        if (Objects.isNull(obj)) {

        }

        return (T) obj;

    }
}
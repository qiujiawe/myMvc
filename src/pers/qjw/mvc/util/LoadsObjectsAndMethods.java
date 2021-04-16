package pers.qjw.mvc.util;

import pers.qjw.mvc.annotations.GetMapping;
import pers.qjw.mvc.annotations.RequestMapping;
import pers.qjw.mvc.annotations.RestController;
import pers.qjw.mvc.bean.UriAndMethodsMapping;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.Objects;

public class LoadsObjectsAndMethods {

    // 因为多个方法中用到了，懒得传来传去，所以加了属性
    private final String packagePath;

    public LoadsObjectsAndMethods(String packagePath) {
        this.packagePath = packagePath;
    }

    // 获取包的绝对路径
    private String getAbsolutePath() {
        String AfterProcessing = packagePath.replace(".", "/");
        URL url = Thread.currentThread().getContextClassLoader().getResource(AfterProcessing);
        if (Objects.isNull(url)) {
            throw new IllegalArgumentException("xml文件里配置了错误的包名:" + packagePath);
        }
        return url.getPath();
    }

    // 获取 资源路径前缀
    private String getUriPrefix(Class<?> clazz) {
        RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
        if (Objects.isNull(requestMapping)) {
            // 没有前缀
            return "";
        } else {
            String uriPrefix = requestMapping.value();
            if (uriPrefix.contains("/")) {
                return uriPrefix;
            } else {
                return "/" + uriPrefix;
            }
        }
    }

    // 通过反射得到文件的 controller类对象 和 处理请求的方法 和 uri
    private void loadObjectsAndMethod(File file) {
        UriAndMethodsMapping uriAndMethodsMapping = (UriAndMethodsMapping) BeanManagement.getBean("uriAndMethodsMapping");
        if (Objects.isNull(uriAndMethodsMapping)) {
            BeanManagement.addBean("uriAndMethodsMapping", new UriAndMethodsMapping());
            uriAndMethodsMapping = (UriAndMethodsMapping) BeanManagement.getBean("uriAndMethodsMapping");
        }
        // 检查文件是否已.class为后缀
        if (!checkFile(file)) {
            return;
        }
        // 获取类全名
        String classFullName = getClassFullName(file);
        try {
            Class<?> clazz = Class.forName(classFullName);
            // 获取 RestController 类注解
            RestController restController = clazz.getAnnotation(RestController.class);
            // 判断是否为 controller 类
            if (!Objects.isNull(restController)) {
                // 创建类对象
                Object obj = createObj(clazz);
                // 获取uri前缀
                String uriPrefix = getUriPrefix(clazz);
                // 开始加载
                Method[] methods = clazz.getMethods();
                for (Method temp :
                        methods) {
                    GetMapping getMapping = temp.getAnnotation(GetMapping.class);
                    RequestMapping requestMapping = temp.getAnnotation(RequestMapping.class);
                    String requestWay;
                    String uri;
                    if (Objects.isNull(getMapping) && Objects.isNull(requestMapping)) {
                        // 不是处理请求的方法
                        return;
                    } else if (!Objects.isNull(getMapping) && !Objects.isNull(requestMapping)) {
                        throw new IllegalArgumentException("方法" + temp.getName() + "上添加了多个mapping注解");
                    } else if (!Objects.isNull(getMapping)) {
                        // 处理 GET 请求方式的方法
                        requestWay = "GET";
                        String uriBody = getMapping.value();
                        if (!Objects.equals("", uriBody) && !uriBody.contains("/")) {
                            uri = uriPrefix + "/" + uriBody;
                        } else {
                            uri = uriPrefix + uriBody;
                        }
                    } else {
                        // 处理 ALL 请求方式的方法
                        requestWay = "ALL";
                        String uriBody = requestMapping.value();
                        if (!Objects.equals("", uriBody) && !uriBody.contains("/")) {
                            uri = uriPrefix + "/" + uriBody;
                        } else {
                            uri = uriPrefix + uriBody;
                        }
                    }
                    Parameter[] parameters = temp.getParameters();
                    String[] parameterNames;
                    if (!Objects.isNull(parameters) && parameters.length != 0) {
                        parameterNames = new String[parameters.length];
                        for (int i = 0; i < parameters.length; i++) {
                            parameterNames[i] = parameters[i].getName();
                        }
                    } else {
                        parameterNames = null;
                    }
                    uriAndMethodsMapping.addMapping(
                            uri
                            , requestWay
                            , temp
                            , obj
                            , parameterNames
                    );

                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("好家伙，你直接手写了个class文件？");
            e.printStackTrace();
        }

    }

    private Object createObj(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        Object result = null;
        for (Constructor<?> constructorTemp :
                constructors) {
            Parameter[] parameters = constructorTemp.getParameters();
            Object[] objects = new Object[parameters.length];
            for (int i = 0; i < objects.length; i++) {
                objects[i] = BeanManagement.getBean(parameters[i].getName());
            }
            try {
                result = constructorTemp.newInstance(objects);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // 获取类全名
    private String getClassFullName(File file) {
        String absolutePath = file.getAbsolutePath();
        int packageSubscript = absolutePath.indexOf(packagePath.replace(".", "\\"));
        // 去掉前缀
        String classPath = absolutePath.substring(packageSubscript);
        // 去掉后缀
        classPath = classPath.replace(".class", "");
        // 把 \ 换成 .
        return classPath.replace("\\", ".");
    }

    // 检查文件是否已.class为后缀
    private boolean checkFile(File file) {
        String name = file.getName();
        return name.contains(".class");
    }

    // 遍历所有文件
    private void func(File file) {
        File[] fs = file.listFiles();
        if (Objects.isNull(fs) || fs.length == 0) {
            throw new IllegalArgumentException("xml文件里配置的包名下没有找到任何类对象:" + packagePath);
        }
        for (File f : fs) {
            if (f.isDirectory())
                func(f);
            if (f.isFile()) {
                // 载入
                loadObjectsAndMethod(f);
            }
        }
    }

    // 加载所有 controller类对象 和 处理请求的方法
    public void loadsObjectsAndMethods() {
        // 获取包的绝对路径
        String path = getAbsolutePath();
        // 遍历所有文件
        func(new File(path));
    }

}

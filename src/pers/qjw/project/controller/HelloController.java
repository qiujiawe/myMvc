package pers.qjw.project.controller;

import pers.qjw.mvc.annotations.RequestMapping;
import pers.qjw.mvc.annotations.RestController;

@RestController
@RequestMapping("hello")
public class HelloController {

    @RequestMapping("world")
    public String hello(String name){
        return "hello " + name;
    }

}

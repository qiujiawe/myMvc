package pers.qjw.project.controller;

import pers.qjw.mvc.annotations.GetMapping;
import pers.qjw.mvc.annotations.PathVariable;
import pers.qjw.mvc.annotations.RequestMapping;
import pers.qjw.mvc.annotations.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/{name}")
    public String hello(@PathVariable String name){
        return "hello " + name;
    }

}

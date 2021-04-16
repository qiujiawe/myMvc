package pers.qjw.project;

import pers.qjw.mvc.annotations.GetMapping;
import pers.qjw.mvc.annotations.RequestMapping;
import pers.qjw.mvc.annotations.RestController;

@RestController
@RequestMapping("test")
public class Test {

    @GetMapping
    public String test(){
        System.out.println("分发成功！");
        return "test";
    }

}

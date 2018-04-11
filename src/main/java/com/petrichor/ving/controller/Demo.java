package com.petrichor.ving.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//注意这个类的注解，在所有的controller里都是要加的
@RestController
public class Demo {

    @CrossOrigin
    @RequestMapping("/demo")
    public String demo(){

        return  "success";
    }
}

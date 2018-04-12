package com.petrichor.ving.controller;

import com.petrichor.ving.dao.RelationRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class Test_Kevin {
    @Autowired
    RelationRepos relationRepos;

    @CrossOrigin
    @RequestMapping("/test")
    public Boolean test(){

        return  true;
    }
}

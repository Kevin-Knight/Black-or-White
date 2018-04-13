package com.petrichor.ving.controller;

import com.petrichor.ving.dao.AlbumRepos;
import com.petrichor.ving.domain.Album;
import com.petrichor.ving.domain.Production;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//注意这个类的注解，在所有的controller里都是要加的
@RestController
public class Demo {
    @Autowired
    AlbumRepos albumRepos;


    @CrossOrigin
    @RequestMapping("/demo")
    public Boolean demo(){
        Album album=new Album();
        album.setaId("test");
        album.setaName("test");
        albumRepos.save(album);
        Optional<Album> optional=albumRepos.findById("test");
        List<Album> list=albumRepos.findByAName("test");
        albumRepos.delete(album);
        return  optional.get()==list.get(0);
    }

    @CrossOrigin
    @RequestMapping("/testForGGL")
    public List<Production> test(){
        List<Production> productions=new ArrayList<>();
        Production production=new Production();
        productions.add(production);
        production=new Production();
        productions.add(production);
        return productions;
    }
}

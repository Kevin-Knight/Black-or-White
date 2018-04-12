package com.petrichor.ving.controller;

import com.petrichor.ving.dao.AlbumRepos;
import com.petrichor.ving.dao.ProductionRepos;
import com.petrichor.ving.dao.RelationRepos;
import com.petrichor.ving.dao.UserRepos;
import com.petrichor.ving.domain.Album;
import com.petrichor.ving.domain.Production;
import com.petrichor.ving.domain.Relation;
import com.petrichor.ving.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/production")
public class ProductionController {
    @Autowired
    ProductionRepos productionRepos;
    @Autowired
    RelationRepos relationRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    AlbumRepos albumRepos;

    @RequestMapping("/findByUserName")  //通过用户名查询作品
    public List<Production> findByUserName(String name){
        List<User> users=userRepos.findByUName(name);//获得用户名对应的所有用户
        List<Production> productions= new ArrayList<>();//用来存储最终作品结果的数组
        for (User user : users) {
            productions.addAll(productionRepos.findByUId(user.getuId()));
        }

        return productions;
    }

    @RequestMapping("/findByAlbumName")     //通过专辑名查询作品
    public List<Production> findByAlbumName(String name){
        List<Album> albums=albumRepos.findByAName(name);//找到这个专辑名对应的所有专辑
        List<Production> productions=new ArrayList<>();//用来存储最终作品结果的数组
        for (Album album : albums){
            List<Relation> relations = relationRepos.findByAId(album.getaId());//获得一个专辑对应的所有关系
            for (Relation relation : relations){
                Optional<Production> productionOpt=productionRepos.findById(relation.getpId());//根据一个关系得到这个专辑的一个作品的id，并查询得到作品，返回Optional对象
                productionOpt.ifPresent(productions::add);//如果查询到了作品，则将其加入productions
            }
        }
        return productions;
    }

    @RequestMapping("/findByPTag")      //通过标签查询作品
    public List<Production> findByPTag(String pTag){
        return productionRepos.findByPTag(pTag);
    }

    @RequestMapping("/findByPName")     //通过作品名查询作品
    public List<Production> findByPName(String name){
        return productionRepos.findByPName(name);
    }

    @RequestMapping("/delete")      //通过作品对象删除作品
    public void delete(Production production){
        productionRepos.delete(production);
    }

    @RequestMapping("/setPVisibility") //更新作品的可见性
    public void setPVisibility(String PID,String visibility){
        Optional<Production> optional=productionRepos.findById(PID);
        if(optional.isPresent()){
            Production production=optional.get();
            production.setpVisibility(visibility);
            productionRepos.save(production);
        }
    }

    @RequestMapping("/setPCover")
    public void setPCover(String PID,String PCover, MultipartFile img){
        Optional<Production> optional=productionRepos.findById(PID);
        if(optional.isPresent()){
            Production production=optional.get();
            production.setpCover(PCover);
            productionRepos.save(production);
        }
    }

}

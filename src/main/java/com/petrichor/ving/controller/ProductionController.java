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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/production")
public class ProductionController {
    //服务器路径
    private final String serverPath="http://47.100.111.185";
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
        List<Production> productions_init=productionRepos.findAll();//初始数据集
        if(pTag.equals("声态"))
            return productions_init;
        List<Production> productions=new ArrayList<>(); //存储处理结果
        for (Production production : productions_init){ //如果某作品的标签串中包含这个标签，则将这个作品放入结果集
            boolean flag=false;
            String[] tags=production.getpTag().split(";");
            for(String tag : tags){
                if(tag.equals(pTag)){
                    flag=true;
                    break;
                }
            }
            if(flag)
                productions.add(production);
        }
        return productions;
    }

    @RequestMapping("/findByPName")     //通过作品名查询作品
    public List<Production> findByPName(String name){
        return productionRepos.findByPName(name);
    }

    @RequestMapping("/delete")      //通过作品对象删除作品
    public void delete(Production production){
        productionRepos.delete(production);
        /*
        作品删除也要删除文件
         */
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

    @RequestMapping("/setPCover")   //更新作品的封面，现在只修改了封面路径，还没有文件相关代码
    public void setPCover(String PID,MultipartFile img){
        Optional<Production> optional=productionRepos.findById(PID);
        if(optional.isPresent()&&!img.isEmpty()){
            Production production=optional.get();   //获得作品对象
            String uId=production.getuId();         //获得作品的作者id

            String separator= File.separator;   //路径分隔符，Linux下是/而Windows下是\
            String coverPath = uId+separator +"production"+separator+"cover.png";//定义文件相对路径
            File dir_upload=new File(serverPath+separator+uId+"production"+separator);
            boolean createStatus=true;
            if(!dir_upload.exists()) {      //如果目标文件夹不存在，则递归创建文件夹
                createStatus=dir_upload.mkdirs();
            }

            if(createStatus){       //如果文件夹创建成功才执行下述操作
                String uploadPath=serverPath+separator+coverPath;//文件路径是文件夹路径加上文件名
                File uploadFile=new File(uploadPath);
                try {
                    BufferedOutputStream out = new BufferedOutputStream(
                            new FileOutputStream(uploadFile)); //根据文件类生成输出流

                    out.write(img.getBytes());//通过输出流将上传的文件写入到文件路径中
                    out.flush();//清空缓存
                    out.close();//关闭输出流

                    production.setpCover(coverPath);
                    productionRepos.save(production);   //上传成功后修改数据库中的文件路径

                } catch (FileNotFoundException e) {//当出现文件找不到错误时执行的代码，一般用不到
                    e.printStackTrace();
                } catch (IOException e) {//当出现输入输出错误时执行的代码，一般用不到
                    e.printStackTrace();
                }
            }

        }


    }

}

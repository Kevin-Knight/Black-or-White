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

    /** 设置作品标签
     * @param pId   作品Id
     * @param pTag  拟设置的标签
     * @return  若设置成功则返回true，否则返回false
     */
    @RequestMapping("/setPTag")
    public boolean setPTag(String pId, String pTag) {
        //若作品Id不存在或标签值为空，则返回false
        Optional<Production> productionOpt = productionRepos.findById(pId);
        if (! productionOpt.isPresent() || pTag.length()==0) return false;

        //设置标签并保存
        Production production = productionOpt.get();
        production.setpTag(pTag);
        productionRepos.save(production);

        //若设置标签失败则返回false
        return productionRepos.findByPId(pId).get().getpTag().equals(pTag);
    }

    /** 设置作品描述
     * @param pId   作品Id
     * @param pDesciption  拟设置的描述
     * @return  若设置成功则返回true，否则返回false
     */
    @RequestMapping("/setPDescription")
    public boolean setPDescription (String pId, String pDesciption) {
        //若作品Id不存在或标签值为空，则返回false
        Optional<Production> productionOpt = productionRepos.findById(pId);
        if (! productionOpt.isPresent() || pDesciption.length()==0) return false;

        //设置描述并保存
        Production production = productionOpt.get();
        production.setpDescription(pDesciption);
        productionRepos.save(production);

        //若设置描述失败则返回false
        return productionRepos.findByPId(pId).get().getpDescription().equals(pDesciption);
    }

    /** 设置作品封面
     * @param pId   作品Id
     * @param cover 作品封面
     * @return  若设置成功则返回true，否则返回false
     */
    @RequestMapping("/setPCover")
    public boolean setPCover (String pId, MultipartFile cover) {
        //若作品Id不存在或标签值为空，则返回false
        Optional<Production> productionOpt = productionRepos.findById(pId);
        if (! productionOpt.isPresent() || cover.isEmpty()) return false;

        //设置封面位置
        String separator= File.separator;
        Production production = productionOpt.get();
        String coverPath = production.getuId() + separator
                + "production" + separator + pId + separator;
        production.setpCover(coverPath + "cover.png");
        productionRepos.save(production);

        //创建封面路径
        File dir_upload=new File(serverPath + coverPath);
        boolean createStatus=true;
        if(!dir_upload.exists()) {
            //如果目标文件夹不存在，则递归创建文件夹
            createStatus=dir_upload.mkdirs();
        }
        //若文件夹创建失败，则返回false
        if (!createStatus) return false;

        //上传封面文件
        String uploadPath=serverPath + separator + coverPath + "cover.png";
        File uploadFile=new File(uploadPath);
        try {
            //根据文件类生成输出流
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(uploadFile));
            //通过输出流将上传的文件写入到文件路径中
            out.write(cover.getBytes());
            //清空缓冲
            out.flush();
            //关闭输出流
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            return false;
        }

        return true;
    }

    /** 自定义作品的封面、标签、描述
     * @param production 拟自定义的作品
     * @param cover 封面图片
     * @return
     */
    @RequestMapping("/setPInfo")
    public boolean setPInfo(Production production, MultipartFile cover) {
        //若作品不存在则返回false
        Optional<Production> productionOpt = productionRepos.findByPId(production.getpId());
        if (!productionOpt.isPresent()) return false;

        //自定义封面
        if (! cover.isEmpty()) {
            //设置封面位置
            String separator= File.separator;
            String coverPath = production.getuId() + separator + "production"
                    + separator + production.getpId() + separator;
            production.setpCover(coverPath + "cover.png");

            //创建封面路径
            File dir_upload=new File(serverPath + coverPath);
            boolean createStatus=true;
            if(!dir_upload.exists()) {
                //如果目标文件夹不存在，则递归创建文件夹
                createStatus=dir_upload.mkdirs();
            }
            //若文件夹创建失败，则返回false
            if (!createStatus) return false;

            //上传封面文件
            String uploadPath=serverPath + separator + coverPath + "cover.png";
            File uploadFile=new File(uploadPath);
            try {
                //根据文件类生成输出流
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(uploadFile));
                //通过输出流将上传的文件写入到文件路径中
                out.write(cover.getBytes());
                //清空缓冲
                out.flush();
                //关闭输出流
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("setPInfo----FileNotFoundException");
                System.err.println(e.getMessage());
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("setPInfo----IOException");
                System.err.println(e.getMessage());
                return false;
            }
        }

        //保存自定义的作品信息
        productionRepos.save(production);

        return productionRepos.findByPId(production.getpId()).get().equals(production);
    }
}

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** 对专辑进行增删改查的操作
 *  addAlbum(User user, Album album) 为指定用户增加新的专辑
 */
@RestController
public class AlbumController {
    @Autowired
    RelationRepos relationRepos;
    @Autowired
    AlbumRepos albumRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    ProductionRepos productionRepos;

    /** 为指定用户添加新的专辑
     * @param album 作品信息
     * @return  若添加成功则返回true，否则返回false
     */
    @CrossOrigin
    @RequestMapping("/addAlbum")
    public boolean addAlbum(Album album) {
        //检查用户是否存在
        Optional<User> userOpt = userRepos.findByUId(album.getuId());
        if (! userOpt.isPresent()) return false;

        //生成不重复的专辑ID并添加专辑信息
        String str = "A-"+ UUID.randomUUID().toString().replace("-", "").substring(0, 10);;
        Optional<Album> albumOpt = albumRepos.findById(str);
        while (albumOpt.isPresent()) {
            str = "A-"+ UUID.randomUUID().toString().replace("-", "").substring(0, 10);;
            albumOpt = albumRepos.findById(str);
        }
        album.setaId(str);
        albumRepos.save(album);
        //检查是否添加成功
        albumOpt = albumRepos.findById(str);
        if (! albumOpt.isPresent()) return false;

        return true;
    }

    /** 删除指定专辑
     * @param aId 作品信息
     * @return  若删除成功则返回true，否则返回false
     */
    @CrossOrigin
    @RequestMapping("/deleteAlbum")
    public boolean deleteAlbum(String aId) {
        //删除专辑-作品关系中的专辑记录
        List<Relation> relations = relationRepos.findByAId(aId);
        for (Relation r: relations) {
            relationRepos.delete(r);
        }
        relations = relationRepos.findByAId(aId);
        if (! relations.isEmpty()) return false;

        //删除专辑信息
        albumRepos.deleteById(aId);
        Optional<Album> albumOpt = albumRepos.findById(aId);
        //检查是否删除成功
        if (albumOpt.isPresent()) return false;
        return true;
    }

    /** 更加专辑名查找专辑
     * @param aName 专辑名
     * @return  返回找到的专辑集合
     */
    @CrossOrigin
    @RequestMapping("/findAlbumsByAlbumName")
    public List<Album> findAlbumsByAlbumName (String aName) {
        List<Album> albums = albumRepos.findByAName(aName);
        return albums;
    }

    /** 根据专辑名查找作品
     * @param aName 专辑名
     * @return  返回找到的作品集合
     */
    @CrossOrigin
    @RequestMapping("/findProductionsByAlbumName")
    public List<Production> findProductionsByAlbumName (String aName) {
        List<Production> productions= new ArrayList<>();
        List<Album> albums = albumRepos.findByAName(aName);
        //若该专辑名不存在则直接返回空集合
        if (albums.isEmpty()) return productions;

        //在Relation表中获取作品id集合
        List<Relation> relations = new ArrayList<>();
        for (Album album: albums) {
            relations.addAll(relationRepos.findByAId(album.getaId()));
        }

        //返回作品集合
        for (Relation relation : relations) {
            Optional<Production> productionOpt = productionRepos.findByPId(relation.getpId());
            if (productionOpt.isPresent()) productions.add(productionOpt.get());
        }
        return productions;
    }

    /** 将作品添加到专辑
     * @param aId   专辑Id
     * @param pIds  作品Id
     * @return  若添加成功则返回true，否则返回false
     */
    @CrossOrigin
    @RequestMapping("/addProductionsToAlbum")
    public boolean addProductionsToAlbum (String aId, List<String> pIds) {
        //若专辑Id不存在或添加的作品集为空，则返回false
        if (! albumRepos.findByAId(aId).isPresent() || pIds.isEmpty()) return false;

        Relation relation = new Relation();
        String str = "R-";
        for (String pId: pIds) {
            //若作品Id对应的作品不存在则返回false
            if (! productionRepos.findByPId(pId).isPresent()) return false;

            str = "R-"+ UUID.randomUUID().toString().replace("-", "").substring(0, 10);;
            while (relationRepos.findByRId(str).isPresent()) {
                str = "R-"+ UUID.randomUUID().toString().replace("-", "").substring(0, 10);;
            }
            relation.setrId(str);
            relation.setaId(aId);
            relation.setpId(pId);
            relationRepos.save(relation);

            //若添加作品至专辑失败，则返回false
            if (! relationRepos.findByRId(str).isPresent()) return false;
        }

        return true;
    }

    /** 将作品移出到专辑
     * @param aId   专辑Id
     * @param pIds  作品Id
     * @return  若添加成功则返回true，否则返回false
     */
    @CrossOrigin
    @RequestMapping("/removeProductionsFromAlbum")
    public boolean removeProductionsFromAlbum(String aId, List<String> pIds) {
        //若专辑Id不存在或添加的作品集为空，则返回false
        if (! albumRepos.findByAId(aId).isPresent() || pIds.isEmpty()) return false;

        Optional<Relation> relationOpt;
        for (String pId: pIds) {
            //若作品Id对应的作品不存在则返回false
            if (! productionRepos.findByPId(pId).isPresent()) return false;

            relationOpt = relationRepos.findByAIdAAndPId(aId, pId);
            //若无相应的专辑-作品关系，则返回false
            if (! relationOpt.isPresent()) return false;
            relationRepos.delete(relationOpt.get());

            //若添加作品移出失败，则返回false
            if (relationRepos.findByAIdAAndPId(aId, pId).isPresent()) return false;
        }
        return true;
    }

}

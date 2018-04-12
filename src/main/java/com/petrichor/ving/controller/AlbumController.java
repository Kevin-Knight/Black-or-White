package com.petrichor.ving.controller;

import com.petrichor.ving.dao.AlbumRepos;
import com.petrichor.ving.dao.RelationRepos;
import com.petrichor.ving.dao.UserRepos;
import com.petrichor.ving.domain.Album;
import com.petrichor.ving.domain.Relation;
import com.petrichor.ving.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param album 作品信息
     * @return  若删除成功则返回true，否则返回false
     */
    @CrossOrigin
    @RequestMapping("/deleteAlbum")
    public boolean deleteAlbum(Album album) {
        //删除专辑-作品关系中的专辑记录
        List<Relation> relations = relationRepos.findByAId(album.getaId());
        for (Relation r: relations) {
            relationRepos.delete(r);
        }
        relations = relationRepos.findByAId(album.getaId());
        if (! relations.isEmpty()) return false;

        //删除专辑信息
        albumRepos.delete(album);
        Optional<Album> albumOpt = albumRepos.findById(album.getaId());
        //检查是否删除成功
        if (albumOpt.isPresent()) return false;
        return true;
    }


}

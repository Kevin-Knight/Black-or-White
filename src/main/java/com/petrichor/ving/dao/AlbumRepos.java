package com.petrichor.ving.dao;

import com.petrichor.ving.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumRepos extends JpaRepository<Album,String>{
    Optional<Album> findByAId(String AId);
    List<Album> findByAName(String AName);
    List<Album> findByUId(String UId);
    List<Album> findByATag(String ATag);
    List<Album> findByACover(String ACover);
}

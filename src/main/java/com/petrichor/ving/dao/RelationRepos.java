package com.petrichor.ving.dao;

import com.petrichor.ving.domain.Relation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelationRepos extends JpaRepository<Relation, String> {
    List<Relation> findByUId(String UId);
    List<Relation> findByAId(String AId);
    List<Relation> findByUIdAndAId(String UId, String AId);
    List<Relation> findByUIdAndPId(String UId, String PId);
    List<Relation> findByUIdAndPIdAndAId(String UId, String AId, String PId);
}

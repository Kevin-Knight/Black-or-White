package com.petrichor.ving.dao;

import com.petrichor.ving.domain.Relation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelationRepos extends JpaRepository<Relation, String> {
    List<Relation> findByAId(String AId);
    List<Relation> findByPId(String PId);
}

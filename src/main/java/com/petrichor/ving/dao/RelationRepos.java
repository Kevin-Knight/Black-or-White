package com.petrichor.ving.dao;

import com.petrichor.ving.domain.Relation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RelationRepos extends JpaRepository<Relation, String> {
    Optional<Relation> findByRId(String RId);
    List<Relation> findByAId(String AId);
    List<Relation> findByPId(String PId);
    Optional<Relation> findByAIdAAndPId(String AId, String PId);
}

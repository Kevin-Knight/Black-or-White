package com.petrichor.ving.dao;

import com.petrichor.ving.domain.Production;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductionRepos extends JpaRepository<Production,String> {
    Optional<Production> findByPId(String PId);
    List<Production> findByPName(String PName);
    List<Production> findByUId(String UId);
    List<Production> findByPTag(String PTag);
    List<Production> findByPCover(String PCover);
    List<Production> findByPDescription(String PDescription);
}

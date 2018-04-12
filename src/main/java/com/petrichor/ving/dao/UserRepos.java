package com.petrichor.ving.dao;

import com.petrichor.ving.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepos extends JpaRepository<User,String> {
    List<User> findByUId(String UId);
    List<User> findByUName(String UName);
    List<User> findByUGender(String UGender);
    List<User> findByUAgeBetween(String low, String high);
}

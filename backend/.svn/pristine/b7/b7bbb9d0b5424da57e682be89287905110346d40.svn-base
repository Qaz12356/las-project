package com.cjxy.las.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cjxy.las.bean.User;

public interface UserReop extends JpaRepository<User, Integer> {

	public List<User> findByUserName(String userName);
    public List<User> findByUserCode(String userCode);
	
	public User findByUserCodeAndUserPassAndRole(String userCode,String userPass,String role);
}


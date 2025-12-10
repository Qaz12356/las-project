package com.cjxy.las.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cjxy.las.bean.LostType;

public interface LostTypeReop extends JpaRepository<LostType, Integer> {

	public List<LostType> findByLostTypeName(String lostTypeName);
	
}
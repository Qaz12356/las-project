package com.cjxy.las.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cjxy.las.bean.Claiminfo;


	public interface ClaiminfoReop extends JpaRepository<Claiminfo, Integer> {

		public List<Claiminfo> findByClaimName(String claimName);
		//public Lost findByLostNameAndlostType(String LostName,String lostType);
		
	}

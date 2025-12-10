package com.cjxy.las.bean;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter
@Setter

public class User {
	@Id  //
	@GeneratedValue(strategy=GenerationType.IDENTITY) //
	private Integer userId;//用户ID
	@Column(length = 50)
	private String userAddr;//用户地址
	@Column(length = 50)
	private String userPhone;//用户电话
	@Column(length = 50)
	private String userName;//用户名称
	@Column(length = 20)
	private String userCode;
	@Column(length = 50)
	private String userPass;
	@Column
	private String role;// admin、leader、common
	
	
	
}
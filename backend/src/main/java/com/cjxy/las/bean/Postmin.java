package com.cjxy.las.bean;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Postmin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 主键生成策略
	private Integer postminId;// 发布寻物启事ID
	@Column(length = 50)
	private String loseAddr;// 丢失地址
	@Column(length = 50)
	private String lostName;// 失物名称
	@Column
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	private Date loseDate;//丢失时间
	@Column(length = 50)
	private String lostDespn;//失物描述
	@Column(length = 50)
	private String userPhone;//联系电话


}

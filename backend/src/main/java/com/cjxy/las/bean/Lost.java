package com.cjxy.las.bean;
import jakarta.persistence.Entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
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
public class Lost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 主键生成策略
	private Integer lostId;// 失物ID
	@Column(length = 50)
	private String lostAddr;// 拾到地址
	@Column(length = 50)
	private String lostName;// 失物名称
	@Column(length = 500)
	private String picPath;// 图片路径
	@Column
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	private Date lostDate;//拾到日期
	@Column(length = 50)
	private String lostDespn;//失物描述
	@Column(length = 50)
	private String numbering;//失物编号
	
	@Column(length = 50)
	private String claimStatus;//认领状态
	@ManyToOne
    @JoinColumn(name = "lostTypeId", referencedColumnName = "lostTypeId") 
    private LostType lostType; // 显式指定关联的是LostType的lostTypeId属性
//	@ManyToOne
//    @JoinColumn(name = "lostTypeId")  // 外键字段名
//    private LostType lostType;        // 关联属性名

	

}
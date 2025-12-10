package com.cjxy.las.bean;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Claiminfo {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer claimId; // 认领Id
    @Column(length = 50)
    private String claimName; 

    @Column(length = 50)
    private String claimTime; // 认领时间
//    @Column(length = 50)
//    private String claimStatus; // 认领状态

    @Column(length = 50)
    private String contactWay; // 联系方式
//    @ManyToOne//
//	@JoinColumn(name=  "userId")
//	private User user;//
    @OneToOne//
	@JoinColumn(name = "lostId")
	private Lost lost;
 
}

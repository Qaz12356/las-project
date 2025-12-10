package com.cjxy.las.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.cjxy.las.bean.Claiminfo;
import com.cjxy.las.bean.Lost;
import com.cjxy.las.dao.ClaiminfoReop;
import com.cjxy.las.dao.base.BaseDao;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
@Service
@Transactional
public class ClaiminfoService {
	

@Resource
	private ClaiminfoReop claiminfoReop;
	@Resource
	private BaseDao dao;
	
	
/**
 * 查询
 * @param claimName
 * @param lostId
 * @param pageNo
 * @param pageSize
 * @return
 * @throws Exception
 */
public Page<Claiminfo> queryClaiminfo(String claimName,Integer lostId,int pageNo,int pageSize)throws Exception{
		String hql = "from Claiminfo a ";
		int i = 1;
		List<Object> params = new ArrayList<>();
			if (StringUtils.hasLength(claimName)) {
				hql = hql + (i == 1 ? " where " : " and ") + "a.claimName like?" + (i++);
				params.add('%'+claimName+'%');
			}
			if (null!=lostId&&lostId!=0) {
				hql = hql + (i == 1 ? " where " : " and ") + "a.lost.lostId = ?" + (i++);
				params.add(lostId);
			}
		Page<Claiminfo> page=dao.findJpaPage(hql, Claiminfo.class, params.toArray(), pageNo, pageSize);
		return page;
	}




/**
 * 增加
 * @param claiminfo
 * @param lost
 * @return
 */
public String addClaiminfo(Claiminfo claiminfo,Lost lost) {
	
	List<Claiminfo> ls=claiminfoReop.findByClaimName(claiminfo.getClaimName());
	if (null!=ls&&ls.size()>0) {
		return claiminfo.getClaimName()+"已经存在";
	}else {
		claiminfo.setLost(lost);
		claiminfoReop.save(claiminfo);
		return "success";
	}
}
/**
 * 修改
 * @param claiminfo
 * @param lost
 * @return
 */
public String updClaiminfo(Claiminfo claiminfo,Lost lost) {
	List<Claiminfo> ls=claiminfoReop.findByClaimName(claiminfo.getClaimName());
	if (null != ls && ls.size() > 0) {
		Claiminfo dt=ls.get(0);
		if(dt.getClaimId()==claiminfo.getClaimId()) {//同一条记录
			claiminfo.setLost(lost);
			claiminfoReop.save(claiminfo);
			return "success";
		}else {
			return claiminfo.getClaimName() + "已经存在";
		}
		
	} else {
		claiminfo.setLost(lost);
		claiminfoReop.save(claiminfo);
		return "success";
	}

}


public String updClaimin(Claiminfo claiminfo,Lost lost) {
	if(claiminfoReop.existsById(claiminfo.getClaimId())) {
		claiminfo.setLost(lost);
		claiminfoReop.save(claiminfo);
		return "success";
	}
	return "修改失败";
}

//public String updClaiminfo(Claiminfo claiminfo) {
//    Claiminfo ls = claiminfoReop.findById(claiminfo.getClaimId()).orElse(null);
//    if (ls == null) {
//        return "员工不存在";
//    }
//
//    // 更新员工信息
//    ls.setClaimName(claiminfo.getClaimName());
//    ls.setContactWay(claiminfo.getContactWay());
//    ls.setLost(claiminfo.getLost());
//
//    claiminfoReop.save(ls);
//    return "success";
//}

//public Claiminfo updateClaimInfo(Integer claimId, Claiminfo updatedClaimInfo) {
//    Optional<Claiminfo> existingClaimInfoOptional = claiminfoReop.findById(claimId);
//    if (existingClaimInfoOptional.isPresent()) {
//        Claiminfo existingClaimInfo = existingClaimInfoOptional.get();
//
//        if (updatedClaimInfo.getClaimName() != null) {
//            existingClaimInfo.setClaimName(updatedClaimInfo.getClaimName());
//        }
//        if (updatedClaimInfo.getClaimTime() != null) {
//            existingClaimInfo.setClaimTime(updatedClaimInfo.getClaimTime());
//        }
//        if (updatedClaimInfo.getContactWay() != null) {
//            existingClaimInfo.setContactWay(updatedClaimInfo.getContactWay());
//        }
//
//        // 处理关联的 Lost 实体
//        if (updatedClaimInfo.getLost() != null && updatedClaimInfo.getLost().getLostId() != null) {
//            existingClaimInfo.setLost(updatedClaimInfo.getLost());
//        }
//
//        return claiminfoReop.save(existingClaimInfo);
//    }
//    return null;
//}


/**
 * 删除
 * @param claimId
 */
public void delClaim(Integer claimId) {
	claiminfoReop.deleteById(claimId);
}

}

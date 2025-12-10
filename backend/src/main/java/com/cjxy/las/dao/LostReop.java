package com.cjxy.las.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cjxy.las.bean.Lost;
public interface LostReop extends JpaRepository<Lost, Integer> {
	
	

	public List<Lost> findByLostName(String lostName);
	// 检查是否存在关联的 lost 记录
	//public Lost findByLostNameAndlostType(String LostName,String lostType);
//	@Query(value = "SELECT \r\n"
//			+ "    lt.lostTypeId,\r\n"
//			+ "    lt.lostTypeName,\r\n"
//			+ "    COUNT(l.lostId) AS value\r\n"
//			+ "FROM \r\n"
//			+ "    losttype lt\r\n"
//			+ "LEFT JOIN \r\n"
//			+ "    lost l ON lt.lostTypeId = l.lostTypeId\r\n"
//			+ "WHERE \r\n"
//			+ "    EXTRACT(MONTH FROM l.lostDate) =:lostDate\r\n"
//			+ "GROUP BY \r\n"
//			+ "    lt.lostTypeId, lt.lostTypeName\r\n"
//			+ "ORDER BY \r\n"
//			+ "    lt.lostTypeId; ", nativeQuery = true)
	@Query(value = "SELECT \r\n"
			+ "    lt.lostTypeId,\r\n"
			+ "    lt.lostTypeName,\r\n"
			+ "    COUNT(l.lostId) AS value\r\n"
			+ "FROM \r\n"
			+ "    LostType lt\r\n"
			+ "LEFT JOIN \r\n"
			+ "    Lost l ON lt.lostTypeId = l.lostTypeId\r\n"
			+ "WHERE \r\n"
			+ "    EXTRACT(MONTH FROM l.lostDate) =:lostDate\r\n"
			+ "GROUP BY \r\n"
			+ "    lt.lostTypeId, lt.lostTypeName\r\n"
			+ "ORDER BY \r\n"
			+ "    lt.lostTypeId; ", nativeQuery = true)
	public List<Map<String, Object>> queryLostTypeSs(Integer lostDate);
}

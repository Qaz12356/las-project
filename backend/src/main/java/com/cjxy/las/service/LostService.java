package com.cjxy.las.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cjxy.las.bean.Lost;
import com.cjxy.las.bean.LostType;
import com.cjxy.las.dao.LostReop;
import com.cjxy.las.dao.base.BaseDao;

import jakarta.transaction.Transactional;
import jakarta.annotation.Resource;

@Service
@Transactional
public class LostService {

    @Resource
    private LostReop lostReop;
    @Resource
    private BaseDao dao;

    @Value("${filepath}")
    private String filepath;

    public List<Map<String, Object>> queryLostTypeSs(Integer lostDate) {
        return lostReop.queryLostTypeSs(lostDate);
    }

    public String saveFile(MultipartFile file, Lost lost, LostType lostType) {
        String fileName = file.getOriginalFilename();
        int index = fileName.lastIndexOf(".");
        String extName = fileName.substring(index, fileName.length());
        String newfileName = UUID.randomUUID() + extName;
        String filePath = filepath + "\\" + newfileName;
        try (InputStream inputStream = file.getInputStream();
             OutputStream outputStream = new FileOutputStream(new File(filePath))) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            lost.setPicPath(newfileName);
            lost.setLostType(lostType);
            lostReop.save(lost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

    public Page<Lost> queryLost(String lostName, Integer lostTypeId, int pageNo, int pageSize) throws Exception {
        String hql = "from Lost a ";
        int i = 1;
        List<Object> params = new ArrayList<>();
        if (StringUtils.hasLength(lostName)) {
            hql = hql + (i == 1? " where " : " and ") + "a.lostName like?" + (i++);
            params.add('%' + lostName + '%');
        }
        if (null!= lostTypeId && lostTypeId!= 0) {
            hql = hql + (i == 1? " where " : " and ") + "a.lostType.lostTypeId =?" + (i++);
            params.add(lostTypeId);
        }
        Page<Lost> page = dao.findJpaPage(hql, Lost.class, params.toArray(), pageNo, pageSize);
        return page;
    }

    public List<Lost> queryAllLost() {
        return lostReop.findAll();
    }

    public void delLost(Integer lostId) {
        lostReop.deleteById(lostId);
    }

    public String updLost(Lost lost) {
        List<Lost> ls = lostReop.findByLostName(lost.getLostName());
        if (null!= ls && ls.size() > 0) {
            Lost dt = ls.get(0);
            if (dt.getLostId() == lost.getLostId()) {
                lostReop.save(lost);
                return "success";
            } else {
                return lost.getLostName() + "已经存在";
            }
        } else {
            lostReop.save(lost);
            return "success";
        }
    }

   

    public boolean addStatus(Integer lostId, String claimStatus) {
        try {
            Lost lost = lostReop.findById(lostId).orElse(null);
            if (lost!= null) {
                lost.setClaimStatus(claimStatus);
                lostReop.save(lost);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String updStatus(Integer lostId, String newClaimStatus) {
        try {
            // 根据 lostId 查找 Lost 对象
            Lost lost = lostReop.findById(lostId).orElse(null);
            if (lost != null) {
                // 获取当前的状态
                String currentStatus = lost.getClaimStatus();
                if (currentStatus.equals(newClaimStatus)) {
                    // 如果新状态和当前状态相同，提示无需更新
                    return "新状态和当前状态相同，无需更新";
                } else {
                    // 更新状态
                    lost.setClaimStatus(newClaimStatus);
                    lostReop.save(lost);
                    return "success";
                }
            } else {
                // 如果未找到对应的 Lost 对象，提示未找到记录
                return "未找到 ID 为 " + lostId + " 的记录";
            }
        } catch (Exception e) {
            // 捕获异常并打印堆栈信息
            e.printStackTrace();
            return "更新过程中出现错误：" + e.getMessage();
        }
    }
}
package com.cjxy.las.action;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cjxy.las.bean.Lost;
import com.cjxy.las.bean.LostType;
import com.cjxy.las.dao.LostTypeReop;
import com.cjxy.las.service.LostService;
import com.cjxy.las.utils.RespBody;

@RestController
@RequestMapping("/api/lost")
public class LostAction {

    @Resource
    @Autowired
    private LostService lostService;
    @Resource
    private LostTypeReop lostTypeReop;
    
   
    @Value("${filepath}")
    private String filepath;

    @PostMapping("/queryLostTypeSs")
    public RespBody<List<Map<String, Object>>> queryLostTypeSs(Integer lostDate) {
        RespBody<List<Map<String, Object>>> body = new RespBody<>();
        try {
            List<Map<String, Object>> ls = lostService.queryLostTypeSs(lostDate);
            body.setCode(RespBody.SUCCESS);
            body.setData(ls);
        } catch (Exception e) {
            e.printStackTrace();
            body.setCode(500, e.toString());
        }
        return body;
    }

    @PostMapping("/findLost")
    public RespBody<Map<String, Object>> findLost(String lostName, Integer lostTypeId, int pageNo, int pageSize) {
        RespBody<Map<String, Object>> body = new RespBody<>();
        try {
            Page<Lost> pg = lostService.queryLost(lostName, lostTypeId, pageNo, pageSize);
            Map<String, Object> data = new HashMap<>();
            data.put("rows", pg.getContent());
            data.put("total", pg.getTotalElements());
            body.setData(data);
            body.setCode(RespBody.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            body.setCode(500, e.toString());
        }
        return body;
    }

    @PostMapping("/findAllLost")
    public RespBody<List<Lost>> findAllLost() {
        RespBody<List<Lost>> body = new RespBody<>();
        try {
            List<Lost> ls = lostService.queryAllLost();
            body.setCode(RespBody.SUCCESS);
            body.setData(ls);
        } catch (Exception e) {
            e.printStackTrace();
            body.setCode(500, e.toString());
        }
        return body;
    }

    @PostMapping("/uploadFile")
    public RespBody<String> uploadFile(MultipartFile file, Lost lost, @RequestParam Integer lostTypeId) {
        RespBody<String> body = new RespBody<>();
        try {
            System.out.println(lost.getLostName());
            if (file == null) {
                body.setCode(500, "文件内容不能为Null");
            } else {
                LostType lostType = getLostTypeById(lostTypeId);
                String mess = lostService.saveFile(file, lost, lostType);
                body.setCode(RespBody.SUCCESS);
                body.setData(mess);
            }
        } catch (Exception e) {
            e.printStackTrace();
            body.setCode(500, e.toString());
        }
        return body;
    }

    private LostType getLostTypeById(Integer lostTypeId) {
        return lostTypeReop.findById(lostTypeId)
               .orElseThrow(() -> new RuntimeException("LostType not found"));
    }

    @GetMapping("/preview/{imageName}")
    public void getImagePreview(@PathVariable String imageName, HttpServletResponse response) {
        String imagePath = filepath + "\\" + imageName;
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            try (FileInputStream fis = new FileInputStream(imageFile);
                 java.io.OutputStream os = response.getOutputStream()) {
                response.setContentType("image/jpeg");
                response.setContentLength((int) imageFile.length());

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // 新增状态方法
    

    @PostMapping("/addStatus")
    public RespBody<Boolean> addStatus(@RequestParam Integer lostId, @RequestParam String claimStatus) {
        RespBody<Boolean> body = new RespBody<>();
        try {
            boolean success = lostService.addStatus(lostId, claimStatus);
            body.setCode(RespBody.SUCCESS);
            body.setData(success);
        } catch (Exception e) {
            e.printStackTrace();
            body.setCode(500, e.toString());
        }
        return body;
    }
    
    @PostMapping("/updStatus")
    public RespBody<Boolean> updStatus(@RequestParam Integer lostId, @RequestParam String newClaimStatus) {
    	//@RequestParam用于从请求 URL 的查询字符串或表单数据中获取参数值。
        RespBody<Boolean> body = new RespBody<>();
        try {
        	String mess = lostService.updStatus(lostId, newClaimStatus);
        	if ("success".equals(mess)) {
                body.setCode(RespBody.SUCCESS);
            } else {
                body.setCode(10001);
            }
        } catch (Exception e) {
            e.printStackTrace();
            body.setCode(500, e.toString());
        }
        return body;
    }

    @PostMapping("/updLost")
    public RespBody<Void> updLost(Lost lost) {
        RespBody<Void> body = new RespBody<>();
        try {
            String mess = lostService.updLost(lost);
            if ("success".equals(mess)) {
                body.setCode(RespBody.SUCCESS);
            } else {
                body.setCode(10001);
            }
        } catch (Exception e) {
            e.printStackTrace();
            body.setCode(500, e.toString());
        }
        return body;
    }

    @PostMapping("/delLost")
    public RespBody<Void> delLost(Integer lostId) {
        RespBody<Void> body = new RespBody<>();
        try {
            lostService.delLost(lostId);
            body.setCode(RespBody.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            body.setCode(500, e.toString());
        }
        return body;
    }
}
package com.cjxy.las.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cjxy.las.bean.User;
import com.cjxy.las.service.UserService;
import com.cjxy.las.utils.RespBody;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
public class UserAction {

	@Resource
	private UserService userService;
	
	
//	@PostMapping("/authenticate")
//	public RespBody<Map<String,String>> authenticate(String userCode,String userPass,String role,HttpServletRequest request) {
//		RespBody<Map<String,String>> body=new RespBody<>();
//		try {
//			Map<String,String> map=new HashMap<>();
//			role= role.equals("user")?"user":"admin";
//			User user=userService.checkUser(userCode, userPass,role);
//
//			if (null!=user) {
//				map.put("userCode", userCode);
//				map.put("userName", user.getUserName());
//				
//				HttpSession session=request.getSession();
//				session.setAttribute("userCode", userCode);
//				session.setAttribute("userName", user.getUserName());
//				
//				body.setCode(RespBody.SUCCESS);
//				body.setData(map);
//			}else {
//				body.setCode(10001);
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			body.setCode(500,e.toString());
//			
//		}
//		return body;
//	}
	@PostMapping("/authenticate")
	public RespBody<Map<String,String>> authenticate(String userCode, String userPass, String role, HttpServletRequest request) {
	    RespBody<Map<String,String>> body = new RespBody<>();
	    try {
	        // 直接调用服务层，不强制转换角色
	        User user = userService.checkUser(userCode, userPass, role);
	        
	        if (user != null) {
	            Map<String,String> map = new HashMap<>();
	            map.put("userCode", user.getUserCode());
	            map.put("userName", user.getUserName());
	            map.put("role", user.getRole()); // 返回数据库中存储的真实角色
	            
	            HttpSession session = request.getSession();
	            session.setAttribute("userCode", user.getUserCode());
	            session.setAttribute("userName", user.getUserName());
	            session.setAttribute("role", user.getRole());
	            
	            body.setCode(RespBody.SUCCESS);
	            body.setData(map);
	        } else {
	            body.setCode(10001);
	            body.setMessage("认证失败：用户名或密码错误");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        body.setCode(500, e.toString());
	    }
	    System.out.println("接收到的参数: userCode=" + userCode + ",userPass=" + userPass + ", role=" + role);
	    return body;
	}
	
	
	@PostMapping("/refreshUser")
	public RespBody<Map<String,String>> refreshUser(HttpServletRequest request) {
		RespBody<Map<String,String>> body=new RespBody<>();
		try {
			Map<String,String> map=new HashMap<>();
			HttpSession session=request.getSession();
			
			String userCode=String.valueOf(session.getAttribute("userCode"));
			String userName=String.valueOf(session.getAttribute("userName"));
			if (null!=userCode&&null!=userName) {
				map.put("userCode", userCode);
				map.put("userName", userName);
				body.setCode(RespBody.SUCCESS);
				body.setData(map);
			}else {
				body.setCode(10001);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			body.setCode(500,e.toString());
			
		}
		return body;
	}
	
	@PostMapping("/findUser")
	public RespBody<List<User>> findUser(String userName) {
		RespBody<List<User>> body=new RespBody<>();
		try {
			List<User>  ls=userService.queryUser(userName);
			body.setCode(RespBody.SUCCESS);
			body.setData(ls);
		} catch (Exception e) {
			e.printStackTrace();
			body.setCode(500,e.toString());
		}
		return body;
	}
	
	
	
	@PostMapping("/addUser")
	public RespBody<Void> addUser(User user) {
		RespBody<Void> body=new RespBody<>();
		try {
			String  mess=userService.addUser(user);
			if ("success".equals(mess)) {
				body.setCode(RespBody.SUCCESS);
			}else {
				body.setCode(10001);
			}
		} catch (Exception e) {
			e.printStackTrace();
			body.setCode(500,e.toString());
			
		}
		return body;
	}
	
	@PostMapping("/updUserInfo")
	public RespBody<Void> updUserInfo(User user) {
		RespBody<Void> body=new RespBody<>();
		try {
			String  mess=userService.updUser(user);
			if ("success".equals(mess)) {
				body.setCode(RespBody.SUCCESS);
			}else {
				body.setCode(10001);
			}
		} catch (Exception e) {
			e.printStackTrace();
			body.setCode(500,e.toString());
			
		}
		return body;
	}
	
	@PostMapping("/updUser")
	public RespBody<Void> updUser(User user) {
		RespBody<Void> body=new RespBody<>();
		try {
			String  mess=userService.updUser(user);
			if ("success".equals(mess)) {
				body.setCode(RespBody.SUCCESS);
			}else {
				body.setCode(10001);
			}
		} catch (Exception e) {
			e.printStackTrace();
			body.setCode(500,e.toString());
			
		}
		return body;
	}
	
	@PostMapping("/delUser")
	public RespBody<Void> delUser(Integer userId) {
		RespBody<Void> body=new RespBody<>();
		try {
			userService.delUser(userId);
			body.setCode(RespBody.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			body.setCode(500,e.toString());
			
		}
		return body;
	}

	/**
	 * 根据用户代码查找用户信息
	 * @param userCode 用户代码
	 * @return 用户信息
	 */
	@PostMapping("/findPer")
	public RespBody<User> findPer(String userCode) {
		RespBody<User> body = new RespBody<>();
		try {
			User user = userService.findPer(userCode);
			if (user != null) {
				body.setCode(RespBody.SUCCESS);
				body.setData(user);
			} else {
				body.setCode(10001);
				body.setMessage("未找到用户信息");
			}
		} catch (Exception e) {
			e.printStackTrace();
			body.setCode(500, e.toString());
		}
		return body;
	}
}

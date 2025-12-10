package com.cjxy.las.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.stereotype.Service;

import com.cjxy.las.bean.User;
import com.cjxy.las.dao.UserReop;

import com.cjxy.las.utils.MD5Util;

import jakarta.transaction.Transactional;
import jakarta.annotation.Resource;

@Service
@Transactional
public class UserService {

	@Resource
	private UserReop userReop;
	
	
//	public User checkUser(String userName, String userPass,String role) {
//		return userReop.findByUserCodeAndUserPassAndRole(userName, MD5Util.string2MD5(userPass),role);
//	}
	public User checkUser(String userCode, String userPass, String role) {
		System.out.println("checkUser 参数: userCode=" + userCode + ", userPass=" + userPass + ", role=" + role);
	    return userReop.findByUserCodeAndUserPassAndRole(
	        userCode, 
	        MD5Util.string2MD5(userPass), 
        role
	    );
//		return userReop.findByUserCodeAndUserPassAndRole(
//		        userCode, 
//		        userPass, // 不再二次加密
//		        role
//		        );
	}



	/**
	 * 查询
	 * @param userName
	 * @return
	 */
	
	public List<User> queryUser(String userName) {

		Sort sort = Sort.by(Sort.Direction.DESC, "userName");
		// 匹配器
		ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreNullValues()
				.withMatcher("userName", GenericPropertyMatchers.contains()).withIgnorePaths("userId");
		User user = new User();
		user.setUserName(userName);
		Example<User> ex = Example.of(user, matcher);
		List<User> ls = userReop.findAll(ex,sort);
		return ls;
	}

	/**
	 * 增加用户（带密码加密）
	 * @param user 用户实体
	 * @return 操作结果
	 */
	public String addUser(User user) {
	    List<User> ls = userReop.findByUserName(user.getUserName());
	    if (null != ls && ls.size() > 0) {
	        return user.getUserName() + "已经存在";
	    } else {
	        // 新增：密码加密处理
	        if (user.getUserPass() != null && !user.getUserPass().isEmpty()) {
	            user.setUserPass(MD5Util.string2MD5(user.getUserPass()));
	        }
	        
	        userReop.save(user);
	        return "success";
	    }
	}
	
	
	/*
	 * 删除
	 */
	public void delUser(Integer userId) {
		userReop.deleteById(userId);
	}
	
	/**
	 * 修改用户（带密码加密，强制保留原密码）
	 */
	public String updUserINfo(User user) {
	    // 1. 先从数据库获取原用户信息，确保密码不被覆盖
	    User originalUser = userReop.findById(user.getUserId()).orElse(null);
	    if (originalUser == null) {
	        return "用户不存在";
	    }

	    // 2. 检查用户名是否冲突（若用户修改了用户名）
	    if (!originalUser.getUserName().equals(user.getUserName())) {
	        List<User> sameNameUsers = userReop.findByUserName(user.getUserName());
	        if (sameNameUsers != null && !sameNameUsers.isEmpty()) {
	            return "用户名已存在";
	        }
	    }

	    // 3. 强制使用原密码，忽略前端传递的 userPass
	    user.setUserPass(originalUser.getUserPass());

	    // 4. 更新其他字段（注意：这里需确保只更新允许的字段，或使用 @DynamicUpdate）
	    originalUser.setUserName(user.getUserName());
	    originalUser.setUserPhone(user.getUserPhone());
	    originalUser.setUserAddr(user.getUserAddr());
	    originalUser.setRole(user.getRole());
	    // 其他需要更新的字段...

	    userReop.save(originalUser);
	    return "success";
	}
	/**
	 * 修改用户（带密码加密）
	 */
	public String updUser(User user) {
	    List<User> ls = userReop.findByUserName(user.getUserName());
	    if (null != ls && ls.size() > 0) {
	        User dt = ls.get(0);
	        if (dt.getUserId() == user.getUserId()) { // 同一条记录
	            // 新增：密码加密处理（只在密码字段有值时加密）
	            if (user.getUserPass() != null && !user.getUserPass().isEmpty()) {
	                // 判断是否需要加密（如果密码已加密会包含特征字符，如$2a$开头的BCrypt）
	                // 由于你使用MD5，这里简化处理（实际生产建议升级到BCrypt）
	                user.setUserPass(MD5Util.string2MD5(user.getUserPass()));
	            }
	            
	            userReop.save(user);
	            return "success";
	        } else {
	            return user.getUserName() + "已经存在";
	        }
	    } else {
	        // 新增：密码加密处理
	        if (user.getUserPass() != null && !user.getUserPass().isEmpty()) {
	            user.setUserPass(MD5Util.string2MD5(user.getUserPass()));
	        }
	        
	        userReop.save(user);
	        return "success";
	    }
	}
	
	public void addDefaultUser() {
		List<User> list = userReop.findByUserCode("admin");
		if (null == list || list.size() == 0) {
			User user = new User();
			user.setUserCode("admin");
			user.setUserName("管理员");
			user.setUserPass(MD5Util.string2MD5("admin"));
			user.setRole("admin");
			userReop.save(user);
		}
	
	
}


	public User findById(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 根据用户代码查找用户信息
	 * @param userCode 用户代码
	 * @return 用户信息
	 */
	public User findPer(String userCode) {
		List<User> users = userReop.findByUserCode(userCode);
		if (users != null && !users.isEmpty()) {
			return users.get(0);
		}
		return null;
	}
}

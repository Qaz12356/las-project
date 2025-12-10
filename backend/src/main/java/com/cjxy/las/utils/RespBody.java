package com.cjxy.las.utils;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;


/**
 * 封装统一消息格式
 * @author ice
 */
@Data
public class RespBody<T> {
	private int code = 200;

	private String message = "成功";
	private T data;

	/**
	 * 成功状态码
	 */
	public static final Integer SUCCESS = 200;

	/**
	 * 失败状态码
	 */
	public static final Integer ERROR = 500;

	private static Map<Integer, String> ERROR_CODE;
	static {
		ERROR_CODE=new HashMap<>();

		ERROR_CODE.put(100, "暂无数据");
		ERROR_CODE.put(200, "成功");
		ERROR_CODE.put(300, "失败");
		ERROR_CODE.put(500, "失败状态码");
		ERROR_CODE.put(10000, "通用错误");
		/// 用户类
		ERROR_CODE.put(10001, "用户名或密码错误");
		ERROR_CODE.put(10002, "登录状态已过期");
		ERROR_CODE.put(10003, "注册用户已存在");
		ERROR_CODE.put(10004, "账号已被锁定,请在一小时后重试");
		ERROR_CODE.put(10005, "旧密码错误");
		ERROR_CODE.put(10006, "用户名已存在");
		ERROR_CODE.put(10007, "ip没有权限");
		ERROR_CODE.put(10008, "token无效");
		ERROR_CODE.put(10009, "用户身份不正确");
		/// 操作权限类
		ERROR_CODE.put(20001, "无操作权限");
		/// 参数类
		ERROR_CODE.put(30001, "非法参数");
		ERROR_CODE.put(30002, "缺少必要参数");
		// 数据操作类
		ERROR_CODE.put(40001, "添加数据失败");
		ERROR_CODE.put(40002, "更新数据失败");
		ERROR_CODE.put(40003, "删除数据失败");
		ERROR_CODE.put(40004, "添加数据失败,对象已经存在，建议修改或者删除");
		ERROR_CODE.put(50001, "不存在的对象");
		ERROR_CODE.put(99999, "无任何资源权限");
		ERROR_CODE.put(99000, "系统错误");

	}

	public RespBody() {
	}

	public RespBody(T date) {
		this.data = date;
	}

	public int getCode() {
		return code;
	}

	public RespBody<?> setCode(int code) {
		this.code = code;
		if (ERROR_CODE.containsKey(code)) {
			setMessage(ERROR_CODE.get(code));
		}
		return this;
	}
	
	public RespBody<?> setCode(int code,String message) {
		this.code = code;
		this.message=message;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public RespBody<?> setData(T data) {
		this.data = data;
		return this;
	}

	public static <T> RespBody<T> def(Class<T> clazz) {
		return new RespBody<>();
	}

	public RespBody<T> ok() {
		setCode(200);
		return this;
	}

	public RespBody<T> error(int code) {
		setCode(code);
		return this;
	}

	public RespBody<T> message(String message) {
		setMessage(message);
		return this;
	}

	public RespBody<T> data(T data) {
		setData(data);
		return this;
	}

	public RespBody<T> back(int code, String message, T data) {
		setCode(code);
		setMessage(message);
		setData(data);
		return this;
	}

	public static <T> Boolean isError(RespBody<T> r) {
		return !isSuccess(r);
	}

	public static <T> Boolean isSuccess(RespBody<T> r) {
		return RespBody.SUCCESS == r.getCode();
	}
}

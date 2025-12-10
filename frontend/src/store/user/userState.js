import { authenticate } from "@/apis/user.js";

export default {
	namespaced: true,
	state: {
		userInfo: {
			userCode: "",
			userName: "",
			role: "" 
		},
		isLogin: false  // 🟢 添加 isLogin 字段
	},
	getters: {
		getUserInfo: function(state) {
			return state.userInfo;
		},
		getUserRole: function(state) {
			return state.userInfo.role;
		}
	},
	mutations: {
		// 🟢 修复：添加 localStorage 保存
		setUserLogin(state, payload) {
			console.log("=== setUserLogin 开始 ===");
			console.log("接收到的 payload:", payload);
			
			// 🟢 构建完整的数据对象
			const userInfo = {
				userCode: payload.userCode || "",
				userName: payload.userName || "",
				role: payload.role || ""  // 确保 role 有值
			};
			
			// 更新 state
			state.userInfo = userInfo;
			state.isLogin = true;
			
			// 🟢 保存到 localStorage
			localStorage.setItem("userInfo", JSON.stringify(userInfo));
			
			console.log("登陆者：", state.userInfo.userName, "角色：", state.userInfo.role);
			console.log("已保存到 localStorage:", localStorage.getItem("userInfo"));
			console.log("=== setUserLogin 结束 ===");
		},
		
		setUserInfo(state, payload) {
			state.userInfo = { ...state.userInfo, ...payload };
			localStorage.setItem("userInfo", JSON.stringify(state.userInfo));
		},
		
		clearUserInfo(state) {
			state.userInfo = {
				userCode: "",
				userName: "",
				role: ""
			};
			state.isLogin = false;
			localStorage.removeItem("userInfo");  // 🟢 只保留这个
		},
		
		initUserInfo(state) {
			console.log("=== initUserInfo 开始 ===");
			const storedUserInfo = localStorage.getItem("userInfo");
			console.log("从 localStorage 读取:", storedUserInfo);
			
			if (storedUserInfo) {
				try {
					const parsedInfo = JSON.parse(storedUserInfo);
					console.log("解析后的用户信息:", parsedInfo);
					
					// 🟢 逐个字段赋值
					state.userInfo.userCode = parsedInfo.userCode || "";
					state.userInfo.userName = parsedInfo.userName || "";
					state.userInfo.role = parsedInfo.role || "";  // 确保 role 被设置
					state.isLogin = !!parsedInfo.userCode;
					
					console.log("初始化完成，用户：", state.userInfo.userName, "角色：", state.userInfo.role);
				} catch (error) {
					console.error("解析 localStorage 数据失败:", error);
				}
			} else {
				console.log("localStorage 中没有用户信息");
			}
			console.log("=== initUserInfo 结束 ===");
		}
	},
	actions: {
		// 🟢 修复：确保 role 被传递
		loginLas(context, param) {
			console.log("=== loginLas 开始 ===");
			console.log("登录参数:", param);
			
			return new Promise((resolve, reject) => {
				authenticate(param.userCode, param.userPass, param.role).then(res => {
					console.log("API 返回数据:", res);
					
					// 🟢 关键修复：确保 role 有值
					const userData = {
						userCode: res.userCode || param.userCode,
						userName: res.userName || param.userName,
						// 优先使用 API 返回的 role，没有则使用参数中的 role
						role: res.role || param.role || (param.userCode === 'admin' ? 'admin' : 'user')
					};
					
					console.log("提交到 mutation 的数据:", userData);
					context.commit("setUserLogin", userData);
					
					// 回调
					if (param.success) {
						param.success(res);
					}
					
					resolve(userData);
				}, err => {
					console.log("登录错误:", err);
					reject(err);
				});
			});
		},
		
		logout(context) {
			context.commit("clearUserInfo");
		},
		
		initAuthState(context) {
			context.commit("initUserInfo");
		}
	}
};
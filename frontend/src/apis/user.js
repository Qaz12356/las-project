import {
	request
} from "@/utils/request.js"
//用户
export const authenticate = (userCode, userPass,role) => {
	return request({
		url: "/user/authenticate",
		method: "post",
		data: {
			userCode,
			userPass,
			role
		}
	});
}
// export function authenticate(data) {
//   return request({
//     url: '/user/authenticate',
//     method: 'post',
//     data: data  // 直接传，不要额外嵌套（比如 { data: data } 会导致后端接不到）
//   });
// }

export const refreshUser = () => {
	return request({
		url: "/user/refreshUser",
		method: "post"
	});
}

//查询
export const findUser = (userName) => {
	return request({
		url: "/user/findUser",
		method: "post",
		data: {
			userName
		}
	});
}

export const addUser = (params) => {
	return request({
		url: "/user/addUser",
		method: "post",
		data: {
			...params
		}
	});
}


export const updUserInfo = (params) => {
	return request({
		url: "/user/updUserInfo",
		method: "post",
		data: params
	});
}

export const updUser = (params) => {
	return request({
		url: "/user/updUser",
		method: "post",
		data: params
	});
}

export const delUser = (userId) => {
	return request({
		url: "/user/delUser",
		method: "post",
		data: {
			userId
		}
	});
}
export const findPer = (userCode) => {
	return request({
		url: "/user/findPer",
		method: "post",
		data: {
			userCode
		}
	});
}
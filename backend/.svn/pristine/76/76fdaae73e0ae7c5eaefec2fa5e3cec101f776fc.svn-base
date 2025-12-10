import {
	request
} from "@/utils/request.js"
//用户
export const authenticate = (adminCode, adminPass) => {
	return request({
		url: "/admin/authenticate",
		method: "post",
		data: {
			adminCode,
			adminPass
		}
	});
}

export const refreshUser = () => {
	return request({
		url: "/admin/refreshUser",
		method: "post"
	});
}
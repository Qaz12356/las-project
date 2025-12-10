import {
	request
} from "@/utils/request.js"


//查询
export const findLostType = (lostTypeName) => {
	return request({
		url: "/lostType/findLostType",
		method: "post",
		data: {
			lostTypeName
		}
	});
}
export const findAllLostType = () => {
	return request({
		url: "/lostType/findAllLostType",
		method: "post"
	});
}

export const addLostType = (params) => {
	return request({
		url: "/lostType/addLostType",
		method: "post",
		data: {
			...params
		}
	});
}


export const updLostType = (params) => {
	return request({
		url: "/lostType/updLostType",
		method: "post",
		data: params
	});
}

export const delLostType = (lostTypeId) => {
	return request({
		url: "/lostType/delLostType",
		method: "post",
		data: {
			lostTypeId
		}
	});
}
import {
	request
} from "@/utils/request.js"


//查询
// export const findLost = (lostName, lostType, pageNo, pageSize) => {
//     return request({
//         url: "/lost/findLost",
//         method: "post",
//         data: {
//             lostName,
//             lostType,
// 			pageNo, 
// 			pageSize
//         }
//     });
// }
export const findAllLost = () => {
	return request({
		url: "/lost/findAllLost",
		method: "post"
	});
}
export const findLost = (param) => {
	return request({
		url: "/lost/findLost",
		method: "post",
		data: {
			...param
		}
	});
}

export const updStatus = (param) => {
	return request({
		url: "/lost/updStatus",
		method: "post",
		data: {
			...param
		}
	});
}

export const addStatus = (param) => {
	return request({
		url: "/lost/addStatus",
		method: "post",
		data: {
			...param
		}
	});
}
 
	
	

export const addLost = (params) => {
	return request({
		url: "/lost/addLost",
		method: "post",
		data: {
			...params
		}
	});
}


export const updLost = (params) => {
	return request({
		url: "/lost/updLost",
		method: "post",
		data: params
	});
}

export const delLost = (lostId) => {
	return request({
		url: "/lost/delLost",
		method: "post",
		data: {
			lostId
		}
	});
}

export const queryLostTypeSs = (lostDate) => {
	return request({
		url: "/lost/queryLostTypeSs",
		method: "post",
		data: {
			lostDate
		}
	});
}
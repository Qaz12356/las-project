import {
	request
} from "@/utils/request.js"


// import axios from 'axios';

// export function updateClaimInfo(data, claimId) {
//     return axios.post('/updateClaimInfo', data, {
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         params: {
//             claimId: claimId
//         }
//     });
// }



export const getByLostId = (params) => {
	return request({
		url: "/claiminfo/getByLostId",
		method: "post",
		data: {
			...params
		}
	});
}
export const findClaiminfo = (params) => {
	return request({
		url: "/claiminfo/findClaiminfo",
		method: "post",
		data: {
			...params
		}
	});
}

export const addClaiminfo = (params) => {
	return request({
		url: "/claiminfo/addClaiminfo",
		method: "post",
		data: {
			...params
		}
	});
}

export const updateClaimInfo = (params) => {
	return request({
		url: "/claiminfo/updateClaimInfo",
		method: "post",
		data: {
			...params
		}
	});
}

export const updClaimin = (params) => {
	return request({
		url: "/claiminfo/updClaimin",
		method: "post",
		data: {
			...params
		}
	});
}

export const updClaiminfo = (params) => {
	return request({
		url: "/claiminfo/updClaiminfo",
		method: "post",
		data: {
			...params
		}
	});
}

export const delClaim = (claimId) => {
	return request({
		url: "/claiminfo/delClaim",
		method: "post",
		data: {
			claimId
		}
	});
}
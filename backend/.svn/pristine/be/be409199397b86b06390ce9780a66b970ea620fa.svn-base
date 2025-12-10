import axios from "axios";
import {
	regCfg,
	checkMode,
	whiteList,
	tokenName
} from "./config.js";
import router from "@/route/router.js";

function request(options) {
	console.log("request:", options);
	return new Promise((resolve, reject) => {
		const instance = axios.create({
			...regCfg
		});
		// request 请求拦截器
		instance.interceptors.request.use(
			(config) => {
				if (checkMode == "jwt") {
					let index = whiteList.findIndex(item => {
						return item == options.url
					})
					if (index > -1) { //在白名单里
						return config;
					} else {
						let token = localStorage.getItem(tokenName);
						// 发送请求时携带token
						if (token) {
							config.headers.authorization = "Bearer " + token;
							return config;
						} else {
							console.log("登录超时，跳转到登录页")
							ElMessageBox.alert("用户令牌失效，请重新登录！", {
								type: 'error',
								callback: (action) => {
									router.push("/")
								}
							})
							return Promise.reject({
								"code": "10008",
								"message": "NO TOKEN"
							});
						}
					}
				} else if (checkMode == "session") {
					return config;
				}
			},
			(error) => {
				// 请求发生错误时
				console.log("request:", error);
				// 判断请求超时
				if (
					error.code === "ECONNABORTED" &&
					error.message.indexOf("timeout") !== -1
				) {
					ElMessageBox.alert('请求超时', {
						type: 'error',
					})
				}

				return Promise.reject(error);
			}
		);

		// response 响应拦截器
		instance.interceptors.response.use(
			(response) => {
				return response.data;
			},
			(err) => {
				if (err && err.response) {
					//根据自己的需求添加状态码
					switch (err.response.status) {
						case 500:
							err.message = "服务器内部错误";
							break;
						case 501:
							err.message = "服务未实现";
							break;
						default:
					}
				}
				if (err.message) {
					if (options.quiet) {
						console.error(response);
					} else {
						ElMessageBox.alert(err.message, {
							type: 'error',
						})
					}

				}
				return Promise.reject(err); // 返回接口返回的错误信息
			}
		);
		// 请求处理，向指向的地址发起请求
		instance(options).then((res) => {
				//这里是对文件下载功能的封装，如不需要可以去掉
				if (options.responseType == "blob") { //文件下载
					//此处返回的blob对象
					var fileURL = window.URL.createObjectURL(new Blob([res]));
					var fileLink = document.createElement('a');
					fileLink.href = fileURL;
					fileLink.setAttribute('download', options.data.fileName);
					document.body.appendChild(fileLink);
					// 点击下载
					fileLink.click();
					// 下载完成移除元素
					document.body.removeChild(fileLink)
					// 释放掉blob对象
					window.URL.revokeObjectURL(fileURL)

					resolve({
						"message": "下载成功",
						"code": 200
					});
				} else {
					/**
					 * response 统一格式
					 * {
					 *    code: 200,
					 *    message: '消息[String]',
					 *    data: '返回数据[Any]'
					 * }
					 * code说明：
					 * 200 成功
					 * -1 失败，可能网络不通，可能后台服务异常或其它异常
					 * -2 登录失效跳回登录
					 * 注意：每个后端系统的格式可能不一致，需要自行调整此处代码
					 */
					console.debug("请求处理：", res)
					if (res.code === 200) {
						resolve(res.data);
					} else {
						// 未登录
						if (res.code === 10002) {
							ElMessageBox.alert("用户登录超时，请重新登录！", {
								type: 'error',
								callback: (action) => {
									router.push("/")
								}
							})
						} else {
							if (options.quiet) {
								reject(res);
							} else {
								let tmp = ""
								if (res == null || res == undefined || undefined == res.message) {
									tmp = "操作失败"
								} else {
									tmp = res.message
								}
								let errMsg = tmp.replace("java.lang.RuntimeException:", "")
									.replace("com.taobao.api.ApiException:", "")
									.replace("java.io.IOException:", "");
								ElMessageBox.alert(errMsg, {
									type: 'error',
									callback: (action) => {
										reject(res);
									}
								})
							}

						}

					}
				}

			})
			.catch((error) => {
				reject(error);
			});
	});
}
export {
	request
}
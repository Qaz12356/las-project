const tokenName = "token" //后端存储在本地localStorage中的名字
const whiteList = [""] //白名单，在此出现的路径，无需携带token，仅checkMode="jwt"有效

//axiox 默认请求配置
const checkMode = "session" //jwt ， session
const regCfg = {
	method: 'post',
	// 基础url前缀
	baseURL: "/las/api",
	// 请求头信息
	headers: {
		//使用application/json格式，后端需要使用通过 request body 获取参数内容
		// 'Content-Type': 'application/json;charset=UTF-8',
		'Content-Type': 'application/x-www-form-urlencoded'
	},
	// 设置超时时间
	timeout: 1000 * 60 * 30,
	// 携带凭证
	withCredentials: true,
	// 返回数据类型
	responseType: 'json'
}
export {
	tokenName,
	whiteList,
	regCfg,
	checkMode
}
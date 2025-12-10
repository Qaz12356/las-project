<template>
	<el-row>
		<el-col :span="10">

		</el-col>
		<el-col :span="9">

			<h3>校园失物招领中心</h3>


		</el-col>
		<el-col :span="3">
			<div style="height: 54px;line-height: 54px; text-align: center;">
				欢迎您：{{userInfo.userName}}
			</div>
		</el-col>
		<el-col :span="1">
			<div style="height: 54px;line-height: 54px; text-align: center;">
				<el-button size="small" @click="showLogoutConfirm">退出登录</el-button>
			</div>
		</el-col>

	</el-row>
</template>

<script setup>
	import {
		refreshUser
	} from "@/apis/user.js";
	import {
		ref,
		onUnmounted
	} from 'vue'; // 补全ref导入
	import {
		useStore
	} from 'vuex'; // 导入Vuex（核心：获取userInfo）
	import {
		useRouter
	} from 'vue-router'; // 导入路由（退出跳转用）
	import {
		ElMessageBox
	} from 'element-plus'; // 导入弹框（退出确认用）
	import axios from 'axios'




	// // 新增：存储所有未完成的请求取消令牌
	const cancelTokens = ref([]);

	// // 封装请求时添加取消令牌（以 axios 为例）
	const request = (config) => {
		const source = axios.CancelToken.source();
		cancelTokens.value.push(source);
		config.cancelToken = source.token;
		return axios(config);
	};
	const store = useStore();
	const router = useRouter();
	let userInfo = computed(() => store.getters["user/getUserInfo"]);
	if (undefined == userInfo.value.userName || userInfo.value.userName == "" || null == userInfo.value.userName) {
		refreshUser().then(res => {
			store.commit("user/setUserLogin", res);
		})
	}
	const showLogoutConfirm = () => {
		ElMessageBox.confirm('确定要退出登录吗？', '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning'
		}).then(() => {
			// 1. 取消所有未完成的请求（避免退出后接口还在执行）
			cancelTokens.value.forEach(source => source.cancel('用户已退出登录'));
			cancelTokens.value = [];

			// 2. 彻底清除所有存储和状态
			localStorage.clear(); // 清空所有本地存储（比单个删除更彻底）
			store.commit("user/clearUserInfo"); // 清空 Vuex 用户信息

			// 3. 强制跳转登录页，并重载页面（避免路由缓存导致的旧状态）
			router.push('/').then(() => {
				window.location.reload(); // 重载页面，清除所有组件缓存
			});
		}).catch(() => {
			console.log('用户取消了退出登录');
		});
	};

	// 组件卸载时清除取消令牌（防止内存泄漏）
	onUnmounted(() => {
		cancelTokens.value.forEach(source => source.cancel());
	});
</script>

<style scoped>
</style>
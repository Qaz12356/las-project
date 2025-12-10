<template>
	<div class="bgbody">
		<div class="login-container">
		<div id="login_box">
			<h2>登录</h2>
			<div class="input_box">
				<input type="text" placeholder="请输入用户名" v-model="userCode">
			</div>
			<div class="input_box">
				<input type="password" placeholder="请输入密码" v-model="userPass">
			</div>
			<div class="input_box">
				<select v-model="role" style="
					border: 0;
					width: 60%;
					font-size: 15px;
					color: #000000;
					background: transparent;
					border-bottom: 2px solid s#55ffff;
					padding: 5px 10px;
					outline: none;
					margin-top: 10px;
				">
					<option value="" disabled>请选择角色</option>
					<option value="user">用户</option>
					<option value="admin">管理员</option>
				</select>
			</div>
			<button @click="doLogin()">登录</button><br>
		</div>
		</div>
	</div>
</template>
<script setup>
	import { ref } from "vue"; 
	import { useRouter } from "vue-router";
	import { useStore } from 'vuex'
	import { ElMessageBox } from 'element-plus'; 

	
	let userCode = ref("");
	let userPass = ref("");
	let role = ref(""); 

	const store = useStore();
	const router = useRouter();

	
	function doLogin() {
		
		if (userCode.value == "" || userPass.value == "" || role.value == "") {
			ElMessageBox.alert('请输入用户名、密码和角色', {
				type: 'warning',
			})
		} else {
			store.dispatch('user/loginLas', {
				userCode: userCode.value,
				userPass: userPass.value,
				role: role.value, 
				success: function(data) {
					
					router.push("/index")
				}
			})
		}
	}
</script>

<style scoped>

.bgbody {
  background: url('@/assets/cjxy.jpg') no-repeat;
  background-size: 100% 130%;
  height: 100%;
  width: 100%;
  position: fixed;
}
.login-container {
  width: 400px;
  margin: 100px auto;
  padding: 20px;
  border: 1px solid #e6e6e6;
  border-radius: 8px;
  background: #fff;
}
.login-form {
  margin-top: 20px;
}
</style>
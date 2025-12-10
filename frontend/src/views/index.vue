<template>
	<div class="page-container">
		<header>
			<keep-alive>
				<pagetop></pagetop>
			</keep-alive>
		</header>
		<main>
		
			<div class="left">
				<pageleft></pageleft>
			</div>
			<div class="right">
				<router-view></router-view>
			</div>
		</main>
	</div>
</template>
<script setup>
	//导入组件
	import pagetop from "@/components/layout/top.vue"
	import pageleft from "@/components/layout/left.vue"
	import {
		computed
	} from 'vue';
	
	
	const store = useStore();
	let userInfo = computed(() => store.getters["user/getUserInfo"])
</script>

<style scoped>
	.page-container {
		display: flex;
		flex-direction: column;
		height: 100%;
		overflow: hidden;
	}

	header {
		height: 45px;
		background-color: #aaffff;
		
		
	}

	main {
		display: flex;
		flex: 1;
		overflow: auto;
	}
	

	main .left {
		height: 100%;
		background-color: #ffffff;
		color: #fff;
	}

	main .right {
		flex: 1;
		overflow: hidden;
		background-color: #f5f7f9;
	}

	main .right .main-body {
		padding: 16px 16px 30px;
		overflow: auto;
		height: 100%;
		box-sizing: border-box;
	}
</style>
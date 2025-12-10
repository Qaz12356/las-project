<template>
 <el-row class="sidebar-container">
  <el-col :span="24">
   <el-menu active-text-color="#409EFF" background-color="#ffffff" class="lost-menu" default-active="1-1"
    text-color="#303133" :router="true">
	<!-- 个人中心 -->
	<el-menu-item index="/perCenter">
	 <el-icon>
	  <User />
	 </el-icon>
	 <span>个人中心</span>
	</el-menu-item>
    <!-- 失物管理模块 -->
    <el-sub-menu index="/index">
     <template #title>
      <el-icon>
       <Collection />
      </el-icon>
      <span>失物管理</span>
     </template>
     <el-menu-item-group title="登记操作">
      <el-menu-item index="/lfRegistration">
       <el-icon>
        <DocumentAdd />
       </el-icon>
       失物登记
      </el-menu-item>
     </el-menu-item-group>
     <el-menu-item-group title="快速寻找">
      <el-menu-item index="/findLost">
       <el-icon>
        <Search />
       </el-icon>
       寻找失物
      </el-menu-item>
     </el-menu-item-group>
    </el-sub-menu>
    <el-menu-item index="/pie">
     <el-icon>
      <User />
     </el-icon>
     <span>数据统计</span>
    </el-menu-item>
    
	
    <el-menu-item index="/Bulletin">
     <el-icon>
      <User />
     </el-icon>
     <span>公告栏</span>
    </el-menu-item>
    <!-- 系统设置：改用Vuex的role判断，必显示 -->
    <el-sub-menu index="5" v-if="vuexRole === 'admin'">
     <template #title>
      <el-icon>
       <Setting />
      </el-icon>
      <span>系统设置</span>
     </template>
     <el-menu-item index="/userManagemente">用户管理</el-menu-item>
     <el-menu-item index="/lostManagement">失物管理</el-menu-item>
     <el-menu-item index="/lostTypeMg">失物类型</el-menu-item>
     <el-menu-item index="/Claiminfo">认领记录</el-menu-item>
    </el-sub-menu>
   </el-menu>
  </el-col>
 </el-row>
</template>

<script setup>
 import { computed, onMounted } from 'vue'; // 新增必要API
 import { useStore } from 'vuex'; // 导入Vuex（核心）
 import {
  School,
  Collection,
  DocumentAdd,
  Search,
  User,
  Setting
 } from '@element-plus/icons-vue'

 // 1. 初始化Vuex仓库
 const store = useStore();

 // 2. 从Vuex取响应式的role（登录后立刻有值，不受localStorage影响）
 const vuexRole = computed(() => {
  return store.state.user.userInfo.role || '';
 });

 // 3. 调试打印（可选，验证role值）
 onMounted(() => {
   console.log('Vuex中的角色：', vuexRole.value);
   
   // 🟢 修正：读取 userInfo，然后获取其中的 role
   const userInfoStr = localStorage.getItem("userInfo");
   let localStorageRole = null;
   
   if (userInfoStr) {
     try {
       const userInfo = JSON.parse(userInfoStr);
       localStorageRole = userInfo.role;
     } catch (error) {
       console.error('解析失败:', error);
     }
   }
   
   console.log('localStorage中的角色：', localStorageRole);
 });
</script>

<style scoped>
	.tac {
		padding: 20px;
		background: #f5f7fa;
		border-radius: 8px;
		max-width: 1200px;
		margin: 20px auto;
		box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
	}

	.mb-2 {
		margin-bottom: 1.5rem !important;
		color: #303133;
		font-size: 18px;
		font-weight: 600;
		letter-spacing: 0.5px;
	}

	.el-col-12 {
		padding: 0 15px;
		transition: transform 0.3s ease;
	}

	.el-col-12:hover {
		transform: translateY(-5px);
	}

	.el-menu-vertical-demo:not(.el-menu--collapse) {
		width: 100%;
		min-height: 400px;
		border-right: none;
		box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
		border-radius: 8px;
		overflow: hidden;
	}

	/* 默认颜色菜单优化 */
	.el-menu {
		--el-menu-active-color: #409EFF;
		--el-menu-hover-bg-color: #ecf5ff;
		transition: all 0.3s ease;
	}

	.el-menu-item {
		font-size: 14px;
		height: 56px;
		line-height: 56px;
		transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
	}

	.el-menu-item:hover {
		background-color: #f5f7fa !important;
		transform: translateX(5px);
	}

	.el-sub-menu__title {
		font-size: 14px !important;
	}

	.el-menu-item-group__title {
		padding: 15px 0 5px 20px !important;
		color: #909399 !important;
		font-size: 12px !important;
	}

	/* 自定义颜色菜单优化 */
	.el-menu[style*="background-color: rgb(84, 92, 100)"] {
		--el-menu-hover-bg-color: rgba(255, 255, 255, 0.1);
		box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.3);
	}

	.el-menu[style*="background-color: rgb(84, 92, 100)"] .el-menu-item:hover {
		background-color: rgba(255, 208, 75, 0.1) !important;
		transform: translateX(5px);
	}

	/* 图标样式优化 */
	.el-icon {
		margin-right: 12px !important;
		font-size: 18px;
		vertical-align: middle;
	}

	/* 响应式处理 */
	@media (max-width: 768px) {
		.el-col-12 {
			width: 100% !important;
			margin-bottom: 20px;
		}

		.tac {
			padding: 15px;
			margin: 10px;
		}
	}
</style>
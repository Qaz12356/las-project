import { createApp } from 'vue'
import router from "./route/router.js"
import store from "./store/store.js"
import App from './App.vue'
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import 'normalize.css/normalize.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import locale from 'element-plus/dist/locale/zh-cn.mjs';

const app = createApp(App);

// 引入element图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
	app.component(key, component)
}

// 中文
app.use(ElementPlus, {
	locale
})

console.log("=== 应用启动，初始化用户状态 ===");

// 🟢 改进的初始化函数
function initializeUser() {
  const stored = localStorage.getItem("userInfo");
  console.log("从 localStorage 读取:", stored);
  
  if (stored) {
    try {
      const userInfo = JSON.parse(stored);
      console.log("解析后的用户信息:", userInfo);
      
      // 🟢 关键修复：确保 store 中 role 被正确设置
      // 直接更新 store 的 state
      store.state.user.userInfo = {
        userCode: userInfo.userCode || "",
        userName: userInfo.userName || "",
        role: userInfo.role || ""  // 确保 role 有值
      };
      
      console.log("用户状态已初始化:", store.state.user.userInfo);
    } catch (error) {
      console.error("初始化失败:", error);
    }
  } else {
    console.log("没有找到保存的用户信息");
  }
}

// 调用初始化
initializeUser();

// 挂载前检查
console.log("挂载前用户信息:", store.state.user.userInfo);

app.use(router).use(store).mount('#app');
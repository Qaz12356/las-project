import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from "unplugin-vue-components/vite"
import { resolve } from 'path'
import { ElementPlusResolver } from "unplugin-vue-components/resolvers"

export default defineConfig({
  define: {
    '__VUE_PROD_HYDRATION_MISMATCH_DETAILS__': false,
    '__VUE_OPTIONS_API__': true,
    '__VUE_PROD_DEVTOOLS__': false
  },
  
  // 重要：GitLab Pages 部署必须设置这个
  base: '/las-project/',
  
  plugins: [
    vue(),
    AutoImport({
      imports: ['vue', 'vue-router', 'vuex'],
      resolvers: [ElementPlusResolver()],
    }), 
    Components({
      resolvers: [ElementPlusResolver()],
    }),
  ],
  
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
    }
  },
  
  server: {
    host: true,
    port: 9090,
    open: true,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:8080/',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      },
    }
  },
  
  build: {
    outDir: 'dist',  // 改为 dist，这是标准输出目录
    sourcemap: false,
    minify: 'terser',
    chunkSizeWarningLimit: 1500,
    emptyOutDir: true,
    terserOptions: {
      compress: {
        drop_console: true,
        drop_debugger: true
      }
    },
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (id.includes('node_modules')) {
            return id.toString().split('node_modules/')[1].split('/')[0].toString();
          }
        },
        chunkFileNames: (chunkInfo) => {
          const facadeModuleId = chunkInfo.facadeModuleId ? chunkInfo.facadeModuleId.split('/') : [];
          const fileName = facadeModuleId[facadeModuleId.length - 2] || '[name]';
          return `js/${fileName}/[name].[hash].js`;
        }
      }
    }
  },
  
  esbuild: {
    drop: ["console", "debugger"]
  },
})
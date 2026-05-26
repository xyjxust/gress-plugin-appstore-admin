import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import svgLoader from 'vite-svg-loader'
import { fileURLToPath, URL } from 'node:url'

/**
 * App Store Admin 插件构建配置
 *
 * 构建流程：
 * 1. 打包为 IIFE 格式
 * 2. 导出工厂函数（不是实例）
 * 3. UMD 包装器自动将 default export 赋值给 window.__GRESS_PLUGIN__
 * 4. 宿主调用工厂函数实例化：pluginModule.default(bridge, properties)
 * 5. CSS 单独输出为 .css 文件
 *
 * 注意：
 * - 插件不会自动实例化
 * - 插件不会自动执行
 * - 只有宿主调用工厂函数时才会创建实例
 * - 使用统一的全局变量名 __GRESS_PLUGIN__，宿主机加载后会立即获取并删除
 */
export default defineConfig({
  plugins: [
    vue(),
    svgLoader({
      defaultImport: 'component' // 默认作为组件导入
    })
  ],
  
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },

  build: {
    minify: false,
    cssCodeSplit: true, // 将所有 CSS 打包到单个文件
    lib: {
      target: 'esnext',
      entry: fileURLToPath(new URL('./src/index.ts', import.meta.url)),
      name: '__GRESS_PLUGIN__',  // 使用统一的全局变量名
      formats: ['iife'],
      fileName: () => 'as-admin-frontend.js'
    },
    rollupOptions: {
      // 由主应用提供这些依赖，避免重复打包
      external: ['vue', 'naive-ui'],
      output: {
        globals: {
          vue: 'Vue',
          'naive-ui': 'NaiveUI'
        },
        exports: 'named'
      }
    }
  }
})

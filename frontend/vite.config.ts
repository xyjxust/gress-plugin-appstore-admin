import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import svgLoader from 'vite-svg-loader'
import { fileURLToPath, URL } from 'node:url'
import { createPluginRollupOptions } from '../../gress/gress-plugin-packages/plugin-vite-externals.ts'

export default defineConfig({
  plugins: [
    vue(),
    svgLoader({
      defaultImport: 'component'
    })
  ],

  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },

  build: {
    minify: true,
    cssCodeSplit: true,
    lib: {
      target: 'esnext',
      entry: fileURLToPath(new URL('./src/index.ts', import.meta.url)),
      name: '__GRESS_PLUGIN__',
      formats: ['iife'],
      fileName: () => 'as-admin-frontend.js'
    },
    rollupOptions: createPluginRollupOptions()
  }
})

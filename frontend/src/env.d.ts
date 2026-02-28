/// <reference types="vite/client" />

/**
 * Vue组件类型声明
 */
declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

/**
 * GressBridge全局类型声明
 */
interface GressBridge {
  http: {
    get<T = any>(url: string): Promise<T>
    post<T = any>(url: string, data?: any): Promise<T>
    put<T = any>(url: string, data?: any): Promise<T>
    delete<T = any>(url: string): Promise<T>
  }
  router: {
    push(path: string): void
    replace(path: string): void
    go(n: number): void
    back(): void
  }
  message: {
    success(content: string): void
    error(content: string): void
    warning(content: string): void
    info(content: string): void
  }
}

declare global {
  interface Window {
    GressBridge: GressBridge
  }
}

export {}

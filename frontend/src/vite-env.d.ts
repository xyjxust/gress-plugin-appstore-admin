/// <reference types="vite/client" />

declare module '*.svg' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent
  export default component
}

declare module '*.svg?component' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent
  export default component
}

declare module '*.svg?raw' {
  const content: string
  export default content
}

declare module '*.svg?url' {
  const content: string
  export default content
}

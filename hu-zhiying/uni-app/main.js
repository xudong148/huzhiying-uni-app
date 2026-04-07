// #ifdef VUE3
import { createSSRApp } from 'vue'
import * as Pinia from 'pinia' // 现代写法：从 pinia 引入所有导出
import App from './App.vue' 
export function createApp() {
  const app = createSSRApp(App)

  // 创建并挂载 Pinia 实例
  const pinia = Pinia.createPinia()
  app.use(pinia)

  return {
    app,
    Pinia // 必须返回给全局，Uni-app 内部需要它来处理 H5 和小程序差异
  }
}
// #endif
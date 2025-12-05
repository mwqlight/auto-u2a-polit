import type { App } from 'vue'

/**
 * 全局组件注册
 */
export function setupGlobalComponents(app: App) {
  // 这里可以注册全局组件
  // 例如：app.component('ComponentName', Component)
  
  // 由于CodeEditor是特定于学习中心的组件，我们将在具体页面中按需引入
  // 这样可以保持代码的模块化和按需加载
  console.log('全局组件配置完成')
}
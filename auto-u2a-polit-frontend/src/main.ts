import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { setupNaiveUI } from './plugins/naive-ui'
import { setupDirectives } from './directives'
import { setupGlobalComponents } from './components'
import './styles/index.scss'

// 创建应用实例
const app = createApp(App)

// 配置Pinia状态管理
const pinia = createPinia()
app.use(pinia)

// 配置路由
app.use(router)

// 配置Naive UI
setupNaiveUI(app)

// 配置全局指令
setupDirectives(app)

// 配置全局组件
setupGlobalComponents(app)

// 挂载应用
app.mount('#app')
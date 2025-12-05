import type { App } from 'vue';
import { setupPermissionDirectives } from './permission';

/**
 * 全局指令配置
 */
export function setupDirectives(app: App) {
  // 注册权限指令
  setupPermissionDirectives(app);
  
  // 可以在这里添加其他全局指令
  // setupOtherDirectives(app);
}
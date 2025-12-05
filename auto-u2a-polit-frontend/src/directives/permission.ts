import type { App, DirectiveBinding } from 'vue';
import { usePermission } from '../composables/usePermission';

/**
 * 权限指令
 * 用于控制元素的显示和隐藏
 */

// 权限指令
const permissionDirective = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const { hasPermissionSync, hasRoleSync } = usePermission();
    
    const { value, arg } = binding;
    
    if (!value) {
      return;
    }
    
    let hasAccess = false;
    
    // 根据指令参数判断是权限还是角色检查
    switch (arg) {
      case 'role':
        hasAccess = hasRoleSync(value);
        break;
      case 'permission':
      default:
        hasAccess = hasPermissionSync(value);
        break;
    }
    
    // 如果没有权限，移除元素
    if (!hasAccess) {
      el.parentNode?.removeChild(el);
    }
  },
  
  updated(el: HTMLElement, binding: DirectiveBinding) {
    const { hasPermissionSync, hasRoleSync } = usePermission();
    
    const { value, arg } = binding;
    
    if (!value) {
      return;
    }
    
    let hasAccess = false;
    
    switch (arg) {
      case 'role':
        hasAccess = hasRoleSync(value);
        break;
      case 'permission':
      default:
        hasAccess = hasPermissionSync(value);
        break;
    }
    
    // 如果元素已经被移除，但权限状态改变，需要重新插入
    if (hasAccess && !el.parentNode) {
      // 这里需要根据实际情况重新插入到正确位置
      console.warn('权限指令：元素已被移除，需要手动重新插入');
    }
  }
};

// 任意权限指令（检查任意权限）
const anyPermissionDirective = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const { hasAnyPermission, hasAnyRole } = usePermission();
    
    const { value, arg } = binding;
    
    if (!value || !Array.isArray(value)) {
      return;
    }
    
    let hasAccess = false;
    
    switch (arg) {
      case 'role':
        hasAccess = hasAnyRole(value);
        break;
      case 'permission':
      default:
        hasAccess = hasAnyPermission(value);
        break;
    }
    
    if (!hasAccess) {
      el.parentNode?.removeChild(el);
    }
  }
};

// 所有权限指令（检查所有权限）
const allPermissionDirective = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const { hasAllPermissions, hasAllRoles } = usePermission();
    
    const { value, arg } = binding;
    
    if (!value || !Array.isArray(value)) {
      return;
    }
    
    let hasAccess = false;
    
    switch (arg) {
      case 'role':
        hasAccess = hasAllRoles(value);
        break;
      case 'permission':
      default:
        hasAccess = hasAllPermissions(value);
        break;
    }
    
    if (!hasAccess) {
      el.parentNode?.removeChild(el);
    }
  }
};

/**
 * 注册权限指令
 */
export function setupPermissionDirectives(app: App) {
  // 基础权限指令 v-permission
  app.directive('permission', permissionDirective);
  
  // 任意权限指令 v-any-permission
  app.directive('any-permission', anyPermissionDirective);
  
  // 所有权限指令 v-all-permission
  app.directive('all-permission', allPermissionDirective);
}

/**
 * 指令使用示例：
 * 
 * 1. 基础权限检查：
 *    <button v-permission="'user:create'">创建用户</button>
 *    <button v-permission:role="'admin'">管理员操作</button>
 * 
 * 2. 任意权限检查：
 *    <button v-any-permission="['user:create', 'user:edit']">创建或编辑</button>
 *    <button v-any-permission:role="['admin', 'editor']">管理员或编辑</button>
 * 
 * 3. 所有权限检查：
 *    <button v-all-permission="['user:create', 'user:edit']">创建和编辑</button>
 *    <button v-all-permission:role="['admin', 'supervisor']">管理员和主管</button>
 */
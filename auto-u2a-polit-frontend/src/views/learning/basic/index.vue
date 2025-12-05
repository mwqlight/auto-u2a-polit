<template>
  <div class="basic-syntax-page">
    <n-page-header
      title="基础语法模块"
      subtitle="Java与Go基础语法对比学习"
      @back="$router.back"
    />
    
    <n-card title="基础语法对比" class="mb-6">
      <n-alert type="info" class="mb-4">
        基础语法模块正在开发中，敬请期待...
      </n-alert>
      
      <n-grid :cols="2" :x-gap="24">
        <n-gi>
          <n-card title="Java基础语法" size="small">
            <n-list>
              <n-list-item>
                <n-text strong>强类型语言</n-text>
                <n-text depth="3">编译时类型检查</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>面向对象</n-text>
                <n-text depth="3">类、继承、多态</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>包管理</n-text>
                <n-text depth="3">Maven/Gradle依赖管理</n-text>
              </n-list-item>
            </n-list>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="Go基础语法" size="small">
            <n-list>
              <n-list-item>
                <n-text strong>静态类型语言</n-text>
                <n-text depth="3">编译时类型检查</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>组合优于继承</n-text>
                <n-text depth="3">接口、结构体组合</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>模块管理</n-text>
                <n-text depth="3">Go Modules依赖管理</n-text>
              </n-list-item>
            </n-list>
          </n-card>
        </n-gi>
      </n-grid>
    </n-card>
  </div>
</template>

<script setup lang="ts">
// 基础语法模块页面
</script>

<style scoped>
.basic-syntax-page {
  max-width: 1200px;
  margin: 0 auto;
}

.mb-6 {
  margin-bottom: 24px;
}

.mb-4 {
  margin-bottom: 16px;
}
</style>
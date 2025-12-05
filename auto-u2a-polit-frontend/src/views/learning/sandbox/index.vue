<template>
  <div class="sandbox-page">
    <n-page-header
      title="代码沙盒模块"
      subtitle="Java与Go代码实时运行和调试"
      @back="$router.back"
    />
    
    <n-card title="交互式代码沙盒" class="mb-6">
      <n-alert type="info" class="mb-4">
        代码沙盒模块正在开发中，敬请期待...
      </n-alert>
      
      <n-grid :cols="2" :x-gap="24">
        <n-gi>
          <n-card title="Java沙盒特性" size="small">
            <n-list>
              <n-list-item>
                <n-text strong>在线编译</n-text>
                <n-text depth="3">实时Java代码编译执行</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>调试支持</n-text>
                <n-text depth="3">断点调试和变量查看</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>依赖管理</n-text>
                <n-text depth="3">Maven依赖自动解析</n-text>
              </n-list-item>
            </n-list>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="Go沙盒特性" size="small">
            <n-list>
              <n-list-item>
                <n-text strong>快速编译</n-text>
                <n-text depth="3">Go语言快速编译执行</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>并发调试</n-text>
                <n-text depth="3">Goroutine并发调试</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>模块支持</n-text>
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
// 代码沙盒模块页面
</script>

<style scoped>
.sandbox-page {
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
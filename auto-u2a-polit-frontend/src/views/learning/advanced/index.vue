<template>
  <div class="advanced-features-page">
    <n-page-header
      title="高级特性模块"
      subtitle="Java与Go高级特性对比学习"
      @back="$router.back"
    />
    
    <n-card title="高级特性对比" class="mb-6">
      <n-alert type="info" class="mb-4">
        高级特性模块正在开发中，敬请期待...
      </n-alert>
      
      <n-grid :cols="2" :x-gap="24">
        <n-gi>
          <n-card title="Java高级特性" size="small">
            <n-list>
              <n-list-item>
                <n-text strong>反射机制</n-text>
                <n-text depth="3">运行时类型信息获取</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>注解处理</n-text>
                <n-text depth="3">元数据编程</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>Lambda表达式</n-text>
                <n-text depth="3">函数式编程支持</n-text>
              </n-list-item>
            </n-list>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="Go高级特性" size="small">
            <n-list>
              <n-list-item>
                <n-text strong>接口实现</n-text>
                <n-text depth="3">隐式接口实现</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>反射机制</n-text>
                <n-text depth="3">reflect包提供反射功能</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>包管理</n-text>
                <n-text depth="3">Go Modules现代化包管理</n-text>
              </n-list-item>
            </n-list>
          </n-card>
        </n-gi>
      </n-grid>
    </n-card>
  </div>
</template>

<script setup lang="ts">
// 高级特性模块页面
</script>

<style scoped>
.advanced-features-page {
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
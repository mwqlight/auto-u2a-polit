<template>
  <div class="data-structures-page">
    <n-page-header
      title="数据结构模块"
      subtitle="Java与Go数据结构对比学习"
      @back="$router.back"
    />
    
    <n-card title="数据结构对比" class="mb-6">
      <n-alert type="info" class="mb-4">
        数据结构模块正在开发中，敬请期待...
      </n-alert>
      
      <n-grid :cols="2" :x-gap="24">
        <n-gi>
          <n-card title="Java数据结构" size="small">
            <n-list>
              <n-list-item>
                <n-text strong>集合框架</n-text>
                <n-text depth="3">List、Set、Map等</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>泛型支持</n-text>
                <n-text depth="3">类型安全的集合</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>并发集合</n-text>
                <n-text depth="3">ConcurrentHashMap等</n-text>
              </n-list-item>
            </n-list>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="Go数据结构" size="small">
            <n-list>
              <n-list-item>
                <n-text strong>切片和映射</n-text>
                <n-text depth="3">slice、map内置类型</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>结构体</n-text>
                <n-text depth="3">自定义复合类型</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>通道</n-text>
                <n-text depth="3">并发通信数据结构</n-text>
              </n-list-item>
            </n-list>
          </n-card>
        </n-gi>
      </n-grid>
    </n-card>
  </div>
</template>

<script setup lang="ts">
// 数据结构模块页面
</script>

<style scoped>
.data-structures-page {
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
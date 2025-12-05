<template>
  <div class="functions-page">
    <n-page-header
      title="函数与方法模块"
      subtitle="Java方法与Go函数对比学习"
      @back="$router.back"
    />
    
    <n-card title="函数与方法对比" class="mb-6">
      <n-alert type="info" class="mb-4">
        函数与方法模块正在开发中，敬请期待...
      </n-alert>
      
      <n-grid :cols="2" :x-gap="24">
        <n-gi>
          <n-card title="Java方法" size="small">
            <n-list>
              <n-list-item>
                <n-text strong>类成员方法</n-text>
                <n-text depth="3">必须属于某个类</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>访问修饰符</n-text>
                <n-text depth="3">public/private/protected</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>方法重载</n-text>
                <n-text depth="3">同名不同参数</n-text>
              </n-n-list-item>
            </n-list>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="Go函数" size="small">
            <n-list>
              <n-list-item>
                <n-text strong>一等公民</n-text>
                <n-text depth="3">函数可以作为参数传递</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>多返回值</n-text>
                <n-text depth="3">函数可以返回多个值</n-text>
              </n-list-item>
              <n-list-item>
                <n-text strong>闭包支持</n-text>
                <n-text depth="3">匿名函数和闭包</n-text>
              </n-list-item>
            </n-list>
          </n-card>
        </n-gi>
      </n-grid>
    </n-card>
  </div>
</template>

<script setup lang="ts">
// 函数与方法模块页面
</script>

<style scoped>
.functions-page {
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
<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <h1 class="error-code">404</h1>
      <h2 class="error-title">页面不存在</h2>
      <p class="error-desc">抱歉，您访问的页面不存在或已被删除</p>
      <div class="actions">
        <el-button type="primary" size="large" @click="goHome">返回首页</el-button>
        <el-button size="large" @click="goBack">返回上一页</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/')
}

const goBack = () => {
  router.back()
}
</script>

<style scoped lang="scss">
.not-found-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

  .not-found-content {
    text-align: center;
    color: #fff;

    .error-code {
      font-size: 120px;
      font-weight: 700;
      margin-bottom: 20px;
      text-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    }

    .error-title {
      font-size: 32px;
      font-weight: 600;
      margin-bottom: 12px;
    }

    .error-desc {
      font-size: 16px;
      opacity: 0.9;
      margin-bottom: 40px;
    }

    .actions {
      display: flex;
      gap: 16px;
      justify-content: center;
    }
  }
}
</style>


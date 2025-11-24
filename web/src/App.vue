<template>
  <router-view />
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useAppStore } from '@/stores/app'

const appStore = useAppStore()

onMounted(() => {
  // 初始化深色模式
  if (appStore.darkMode) {
    document.documentElement.classList.add('dark')
  }
})
</script>

<style scoped></style>


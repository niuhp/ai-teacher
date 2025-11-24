<template>
  <div class="main-layout">
    <!-- 侧边栏 -->
    <aside class="sidebar" :class="{ collapsed: appStore.sidebarCollapsed }">
      <div class="logo">
        <img src="/logo.svg" alt="AI教师" class="logo-img" />
        <span v-if="!appStore.sidebarCollapsed" class="logo-text">AI教师</span>
      </div>

      <nav class="nav-menu">
        <router-link to="/chat" class="nav-item" active-class="active">
          <el-icon><ChatDotRound /></el-icon>
          <span v-if="!appStore.sidebarCollapsed">智能问答</span>
        </router-link>
        <router-link to="/history" class="nav-item" active-class="active">
          <el-icon><Clock /></el-icon>
          <span v-if="!appStore.sidebarCollapsed">对话历史</span>
        </router-link>
        <router-link to="/profile" class="nav-item" active-class="active">
          <el-icon><User /></el-icon>
          <span v-if="!appStore.sidebarCollapsed">个人中心</span>
        </router-link>
      </nav>

      <div class="sidebar-footer">
        <el-tooltip :content="appStore.sidebarCollapsed ? '展开' : '收起'" placement="right">
          <div class="nav-item" @click="appStore.toggleSidebar">
            <el-icon><Fold v-if="!appStore.sidebarCollapsed" /><Expand v-else /></el-icon>
            <span v-if="!appStore.sidebarCollapsed">收起</span>
          </div>
        </el-tooltip>
      </div>
    </aside>

    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 头部 -->
      <header class="header">
        <div class="header-left">
          <h2 class="page-title">{{ pageTitle }}</h2>
        </div>
        <div class="header-right">
          <el-tooltip content="切换主题">
            <el-button circle @click="appStore.toggleDarkMode">
              <el-icon><Sunny v-if="!appStore.darkMode" /><Moon v-else /></el-icon>
            </el-button>
          </el-tooltip>
          <el-dropdown @command="handleCommand">
            <div class="user-avatar">
              <el-avatar :src="userStore.userInfo?.avatar" :size="36">
                {{ userStore.userInfo?.nickname?.charAt(0) }}
              </el-avatar>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item disabled>
                  <div class="user-info-dropdown">
                    <div class="nickname">{{ userStore.userInfo?.nickname }}</div>
                    <div class="grade-info">
                      {{ userStore.userInfo?.provinceName }} •
                      {{ userStore.userInfo?.gradeName }}
                    </div>
                  </div>
                </el-dropdown-item>
                <el-dropdown-item divided command="profile">
                  <el-icon><User /></el-icon>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <!-- 页面内容 -->
      <div class="page-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { useAppStore } from '@/stores/app'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const appStore = useAppStore()

const pageTitle = computed(() => {
  return (route.meta.title as string) || 'AI教师'
})

const handleCommand = async (command: string) => {
  if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
      await userStore.logout()
      ElMessage.success('已退出登录')
      router.push('/login')
    } catch (error) {
      // 用户取消
    }
  }
}
</script>

<style scoped lang="scss">
.main-layout {
  display: flex;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
}

.sidebar {
  width: 240px;
  height: 100%;
  background: #fff;
  border-right: 1px solid #e5e7eb;
  display: flex;
  flex-direction: column;
  transition: width 0.3s;

  &.collapsed {
    width: 64px;

    .logo-text {
      display: none;
    }

    .nav-item span {
      display: none;
    }
  }

  .logo {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    padding: 0 20px;
    border-bottom: 1px solid #e5e7eb;

    .logo-img {
      width: 32px;
      height: 32px;
    }

    .logo-text {
      font-size: 18px;
      font-weight: 600;
      color: #409eff;
    }
  }

  .nav-menu {
    flex: 1;
    padding: 20px 12px;
    overflow-y: auto;
  }

  .nav-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 16px;
    margin-bottom: 8px;
    border-radius: 8px;
    color: #606266;
    text-decoration: none;
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      background: #f5f7fa;
      color: #409eff;
    }

    &.active {
      background: #ecf5ff;
      color: #409eff;
      font-weight: 500;
    }

    .el-icon {
      font-size: 20px;
    }
  }

  .sidebar-footer {
    padding: 12px;
    border-top: 1px solid #e5e7eb;
  }
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;

  .header {
    height: 60px;
    background: #fff;
    border-bottom: 1px solid #e5e7eb;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 24px;

    .header-left {
      .page-title {
        font-size: 18px;
        font-weight: 500;
        color: #303133;
      }
    }

    .header-right {
      display: flex;
      align-items: center;
      gap: 16px;

      .user-avatar {
        cursor: pointer;
        transition: transform 0.3s;

        &:hover {
          transform: scale(1.05);
        }
      }
    }
  }

  .page-content {
    flex: 1;
    overflow-y: auto;
    padding: 24px;
  }
}

.user-info-dropdown {
  .nickname {
    font-weight: 500;
    margin-bottom: 4px;
  }

  .grade-info {
    font-size: 12px;
    color: #909399;
  }
}

// 深色模式
.dark {
  .main-layout {
    background: #141414;
  }

  .sidebar {
    background: #1a1a1a;
    border-right-color: #2a2a2a;

    .logo {
      border-bottom-color: #2a2a2a;
    }

    .nav-item {
      color: #e5e5e5;

      &:hover {
        background: #2a2a2a;
      }

      &.active {
        background: #1a3a5a;
      }
    }

    .sidebar-footer {
      border-top-color: #2a2a2a;
    }
  }

  .main-content {
    .header {
      background: #1a1a1a;
      border-bottom-color: #2a2a2a;

      .page-title {
        color: #e5e5e5;
      }
    }
  }
}

// 过渡动画
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>


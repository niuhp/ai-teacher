<template>
  <div class="profile-container">
    <div class="profile-header">
      <h2>个人中心</h2>
    </div>

    <div class="profile-content">
      <!-- 用户信息卡片 -->
      <el-card class="profile-card">
        <template #header>
          <div class="card-header">
            <el-icon><User /></el-icon>
            <span>基本信息</span>
          </div>
        </template>

        <div class="user-info-section">
          <div class="avatar-section">
            <el-avatar :src="userStore.userInfo?.avatar" :size="80">
              {{ userStore.userInfo?.nickname?.charAt(0) }}
            </el-avatar>
            <p class="nickname">{{ userStore.userInfo?.nickname }}</p>
          </div>

          <el-form :model="profileForm" label-width="100px" label-position="left">
            <el-form-item label="昵称">
              <el-input v-model="profileForm.nickname" placeholder="请输入昵称" />
            </el-form-item>

            <el-form-item label="手机号">
              <el-input v-model="profileForm.phone" placeholder="请输入手机号" />
            </el-form-item>

            <el-form-item label="省份">
              <el-select v-model="profileForm.provinceCode" placeholder="请选择省份" style="width: 100%">
                <el-option
                  v-for="province in PROVINCES"
                  :key="province.code"
                  :label="province.name"
                  :value="province.code"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="年级">
              <el-select v-model="profileForm.grade" placeholder="请选择年级" style="width: 100%">
                <el-option
                  v-for="grade in GRADES"
                  :key="grade.code"
                  :label="grade.name"
                  :value="grade.code"
                />
              </el-select>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" :loading="saving" @click="handleSave">保存修改</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-card>

      <!-- 账号设置卡片 -->
      <el-card class="settings-card">
        <template #header>
          <div class="card-header">
            <el-icon><Setting /></el-icon>
            <span>账号设置</span>
          </div>
        </template>

        <div class="settings-section">
          <div class="setting-item">
            <div class="setting-info">
              <h4>深色模式</h4>
              <p>切换为深色主题，保护眼睛</p>
            </div>
            <el-switch v-model="appStore.darkMode" @change="appStore.toggleDarkMode" />
          </div>

          <el-divider />

          <div class="setting-item">
            <div class="setting-info">
              <h4>退出登录</h4>
              <p>退出当前账号</p>
            </div>
            <el-button type="danger" @click="handleLogout">退出</el-button>
          </div>
        </div>
      </el-card>

      <!-- 使用统计卡片 -->
      <el-card class="stats-card">
        <template #header>
          <div class="card-header">
            <el-icon><TrendCharts /></el-icon>
            <span>使用统计</span>
          </div>
        </template>

        <div class="stats-section">
          <div class="stat-item">
            <div class="stat-value">0</div>
            <div class="stat-label">总对话数</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">0</div>
            <div class="stat-label">总提问数</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">0</div>
            <div class="stat-label">收藏数</div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Setting, TrendCharts } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { useAppStore } from '@/stores/app'
import { PROVINCES, GRADES } from '@/utils/constants'

const router = useRouter()
const userStore = useUserStore()
const appStore = useAppStore()

const saving = ref(false)
const profileForm = ref({
  nickname: '',
  phone: '',
  provinceCode: '',
  grade: '',
})

const loadUserInfo = () => {
  if (userStore.userInfo) {
    profileForm.value = {
      nickname: userStore.userInfo.nickname || '',
      phone: userStore.userInfo.phone || '',
      provinceCode: userStore.userInfo.provinceCode || '',
      grade: userStore.userInfo.grade || '',
    }
  }
}

const handleSave = async () => {
  try {
    saving.value = true
    await userStore.updateUser(profileForm.value)
    ElMessage.success('保存成功')
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败，请重试')
  } finally {
    saving.value = false
  }
}

const handleLogout = async () => {
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

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped lang="scss">
.profile-container {
  max-width: 1200px;
  margin: 0 auto;

  .profile-header {
    margin-bottom: 24px;

    h2 {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
    }
  }

  .profile-content {
    display: grid;
    gap: 24px;

    .card-header {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 16px;
      font-weight: 500;
    }

    .profile-card {
      .user-info-section {
        .avatar-section {
          display: flex;
          flex-direction: column;
          align-items: center;
          margin-bottom: 32px;

          .nickname {
            margin-top: 12px;
            font-size: 18px;
            font-weight: 500;
            color: #303133;
          }
        }
      }
    }

    .settings-card {
      .settings-section {
        .setting-item {
          display: flex;
          align-items: center;
          justify-content: space-between;
          padding: 16px 0;

          .setting-info {
            h4 {
              font-size: 16px;
              font-weight: 500;
              color: #303133;
              margin-bottom: 4px;
            }

            p {
              font-size: 14px;
              color: #909399;
            }
          }
        }
      }
    }

    .stats-card {
      .stats-section {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 24px;

        .stat-item {
          text-align: center;

          .stat-value {
            font-size: 32px;
            font-weight: 600;
            color: #409eff;
            margin-bottom: 8px;
          }

          .stat-label {
            font-size: 14px;
            color: #909399;
          }
        }
      }
    }
  }
}

// 响应式
@media (max-width: 768px) {
  .profile-content {
    .stats-card {
      .stats-section {
        grid-template-columns: 1fr;
      }
    }
  }
}

// 深色模式
.dark {
  .profile-container {
    .profile-header {
      h2 {
        color: #e5e5e5;
      }
    }

    .profile-content {
      .profile-card,
      .settings-card,
      .stats-card {
        .card-header {
          color: #e5e5e5;
        }
      }

      .profile-card {
        .user-info-section {
          .avatar-section {
            .nickname {
              color: #e5e5e5;
            }
          }
        }
      }

      .settings-card {
        .settings-section {
          .setting-item {
            .setting-info {
              h4 {
                color: #e5e5e5;
              }
            }
          }
        }
      }
    }
  }
}
</style>


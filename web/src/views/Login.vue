<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <img src="/logo.svg" alt="AI教师" class="logo" />
        <h1 class="title">AI教师</h1>
        <p class="subtitle">智能学习助手，随时为你答疑解惑</p>
      </div>

      <div class="login-content">
        <div v-if="qrCodeStatus === QrCodeStatus.PENDING || qrCodeStatus === QrCodeStatus.SCANNED" class="qrcode-section">
          <div class="qrcode-wrapper">
            <img v-if="qrcodeUrl" :src="qrcodeUrl" alt="登录二维码" class="qrcode" />
            <div v-else class="qrcode-loading">
              <el-icon class="is-loading"><Loading /></el-icon>
              <p>生成二维码中...</p>
            </div>
            
            <!-- 已扫码遮罩 -->
            <div v-if="qrCodeStatus === QrCodeStatus.SCANNED" class="scan-mask">
              <el-icon class="success-icon"><SuccessFilled /></el-icon>
              <p>已扫码，请在手机上确认</p>
            </div>
          </div>

          <div class="tips">
            <el-icon><InfoFilled /></el-icon>
            <span>请使用微信扫描二维码登录</span>
          </div>

          <div class="qrcode-footer">
            <el-button type="primary" link @click="refreshQrCode">
              <el-icon><Refresh /></el-icon>
              刷新二维码
            </el-button>
          </div>
        </div>

        <div v-else-if="qrCodeStatus === QrCodeStatus.EXPIRED" class="status-section">
          <el-icon class="status-icon warning"><WarningFilled /></el-icon>
          <p class="status-text">二维码已过期</p>
          <el-button type="primary" @click="refreshQrCode">重新获取</el-button>
        </div>

        <div v-else-if="qrCodeStatus === QrCodeStatus.CANCELLED" class="status-section">
          <el-icon class="status-icon error"><CircleCloseFilled /></el-icon>
          <p class="status-text">登录已取消</p>
          <el-button type="primary" @click="refreshQrCode">重新扫码</el-button>
        </div>

        <div v-else-if="loading" class="status-section">
          <el-icon class="is-loading status-icon"><Loading /></el-icon>
          <p class="status-text">登录中...</p>
        </div>
      </div>

      <div class="login-footer">
        <p>首次登录将自动创建账号</p>
      </div>
    </div>

    <!-- 功能介绍 -->
    <div class="features">
      <div class="feature-item">
        <el-icon class="feature-icon"><ChatDotRound /></el-icon>
        <h3>智能问答</h3>
        <p>支持文字、图片、语音等多种提问方式</p>
      </div>
      <div class="feature-item">
        <el-icon class="feature-icon"><Reading /></el-icon>
        <h3>教材同步</h3>
        <p>覆盖全国各省份各年级教材知识点</p>
      </div>
      <div class="feature-item">
        <el-icon class="feature-icon"><TrendCharts /></el-icon>
        <h3>分步讲解</h3>
        <p>详细的解题步骤，帮助理解知识点</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { generateQrCode, getQrCodeStatus } from '@/api/auth.mock'
import { useUserStore } from '@/stores/user'
import { QrCodeStatus } from '@/types'

const router = useRouter()
const userStore = useUserStore()

const qrcodeUrl = ref('')
const ticket = ref('')
const qrCodeStatus = ref<QrCodeStatus>(QrCodeStatus.PENDING)
const loading = ref(false)
let pollingTimer: number | null = null

// 生成二维码
const initQrCode = async () => {
  try {
    loading.value = true
    const data = await generateQrCode()
    qrcodeUrl.value = data.qrcodeUrl
    ticket.value = data.ticket
    qrCodeStatus.value = QrCodeStatus.PENDING
    
    // 开始轮询
    startPolling()
  } catch (error) {
    console.error('生成二维码失败:', error)
    ElMessage.error('生成二维码失败，请刷新重试')
  } finally {
    loading.value = false
  }
}

// 开始轮询登录状态
const startPolling = () => {
  if (pollingTimer) {
    clearInterval(pollingTimer)
  }

  pollingTimer = window.setInterval(async () => {
    try {
      const data = await getQrCodeStatus(ticket.value)
      qrCodeStatus.value = data.status

      if (data.status === QrCodeStatus.CONFIRMED) {
        // 登录成功
        stopPolling()
        loading.value = true
        
        if (data.token && data.userInfo) {
          userStore.login(data.token, data.userInfo)
          ElMessage.success('登录成功')
          router.push('/')
        }
      } else if (data.status === QrCodeStatus.EXPIRED || data.status === QrCodeStatus.CANCELLED) {
        // 已过期或已取消
        stopPolling()
      }
    } catch (error) {
      console.error('轮询登录状态失败:', error)
      stopPolling()
    }
  }, 2000) // 每2秒轮询一次
}

// 停止轮询
const stopPolling = () => {
  if (pollingTimer) {
    clearInterval(pollingTimer)
    pollingTimer = null
  }
}

// 刷新二维码
const refreshQrCode = () => {
  stopPolling()
  qrcodeUrl.value = ''
  initQrCode()
}

onMounted(() => {
  initQrCode()
})

onUnmounted(() => {
  stopPolling()
})
</script>

<style scoped lang="scss">
.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 40px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 100%;
  max-width: 420px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  padding: 48px 40px;
  margin-bottom: 60px;

  .login-header {
    text-align: center;
    margin-bottom: 40px;

    .logo {
      width: 64px;
      height: 64px;
      margin-bottom: 16px;
    }

    .title {
      font-size: 28px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 8px;
    }

    .subtitle {
      font-size: 14px;
      color: #909399;
    }
  }

  .login-content {
    .qrcode-section {
      .qrcode-wrapper {
        position: relative;
        width: 240px;
        height: 240px;
        margin: 0 auto 24px;
        border: 1px solid #e5e7eb;
        border-radius: 12px;
        overflow: hidden;

        .qrcode {
          width: 100%;
          height: 100%;
          object-fit: contain;
        }

        .qrcode-loading {
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          height: 100%;
          color: #909399;

          .el-icon {
            font-size: 32px;
            margin-bottom: 12px;
          }
        }

        .scan-mask {
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: rgba(255, 255, 255, 0.95);
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          color: #67c23a;

          .success-icon {
            font-size: 48px;
            margin-bottom: 12px;
          }

          p {
            font-size: 14px;
            font-weight: 500;
          }
        }
      }

      .tips {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 8px;
        color: #909399;
        font-size: 14px;
        margin-bottom: 16px;

        .el-icon {
          font-size: 16px;
        }
      }

      .qrcode-footer {
        text-align: center;
      }
    }

    .status-section {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 40px 0;

      .status-icon {
        font-size: 64px;
        margin-bottom: 16px;

        &.warning {
          color: #e6a23c;
        }

        &.error {
          color: #f56c6c;
        }
      }

      .status-text {
        font-size: 16px;
        color: #606266;
        margin-bottom: 24px;
      }
    }
  }

  .login-footer {
    text-align: center;
    margin-top: 24px;
    padding-top: 24px;
    border-top: 1px solid #e5e7eb;

    p {
      font-size: 12px;
      color: #909399;
    }
  }
}

.features {
  display: flex;
  gap: 32px;
  max-width: 900px;

  .feature-item {
    flex: 1;
    text-align: center;
    color: #fff;

    .feature-icon {
      font-size: 48px;
      margin-bottom: 16px;
      opacity: 0.9;
    }

    h3 {
      font-size: 18px;
      font-weight: 600;
      margin-bottom: 8px;
    }

    p {
      font-size: 14px;
      opacity: 0.8;
      line-height: 1.6;
    }
  }
}

// 响应式
@media (max-width: 768px) {
  .login-card {
    padding: 32px 24px;
  }

  .features {
    flex-direction: column;
    gap: 24px;
    padding: 0 20px;
  }
}

// 深色模式
.dark {
  .login-card {
    background: #1a1a1a;

    .login-header {
      .title {
        color: #e5e5e5;
      }
    }

    .login-content {
      .qrcode-section {
        .qrcode-wrapper {
          border-color: #2a2a2a;
        }
      }

      .status-section {
        .status-text {
          color: #e5e5e5;
        }
      }
    }

    .login-footer {
      border-top-color: #2a2a2a;
    }
  }
}
</style>


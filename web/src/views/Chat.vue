<template>
  <div class="chat-container">
    <!-- 配置面板 -->
    <div v-if="!currentConversationId" class="config-panel">
      <div class="welcome-section">
        <h1 class="welcome-title">👋 你好，我是AI教师</h1>
        <p class="welcome-subtitle">我可以帮助你解答各学科的问题，支持文字、图片、文件等多种提问方式</p>
      </div>

      <el-card class="config-card">
        <template #header>
          <div class="card-header">
            <el-icon><Setting /></el-icon>
            <span>选择学习信息</span>
          </div>
        </template>

        <el-form :model="configForm" label-width="80px" label-position="left">
          <el-form-item label="省份">
            <el-select v-model="configForm.provinceCode" placeholder="请选择省份" style="width: 100%">
              <el-option
                v-for="province in PROVINCES"
                :key="province.code"
                :label="province.name"
                :value="province.code"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="年级">
            <el-select v-model="configForm.grade" placeholder="请选择年级" style="width: 100%">
              <el-option
                v-for="grade in GRADES"
                :key="grade.code"
                :label="grade.name"
                :value="grade.code"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="学科">
            <el-radio-group v-model="configForm.subject" class="subject-radio-group">
              <el-radio-button
                v-for="subject in SUBJECTS"
                :key="subject.code"
                :label="subject.code"
              >
                <span class="subject-icon">{{ subject.icon }}</span>
                {{ subject.name }}
              </el-radio-button>
            </el-radio-group>
          </el-form-item>
        </el-form>

        <el-button
          type="primary"
          size="large"
          :disabled="!canStartChat"
          :loading="conversationStore.loading"
          style="width: 100%"
          @click="startNewChat"
        >
          开始对话
        </el-button>
      </el-card>

      <!-- 快捷提示 -->
      <div class="quick-tips">
        <h3>💡 使用技巧</h3>
        <div class="tips-grid">
          <div class="tip-item">
            <el-icon><EditPen /></el-icon>
            <p>支持输入文字描述问题</p>
          </div>
          <div class="tip-item">
            <el-icon><Picture /></el-icon>
            <p>支持上传图片识别题目</p>
          </div>
          <div class="tip-item">
            <el-icon><Document /></el-icon>
            <p>支持上传文件批量提问</p>
          </div>
          <div class="tip-item">
            <el-icon><ChatDotRound /></el-icon>
            <p>支持连续追问，深入理解</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 对话区域 -->
    <div v-else class="chat-panel">
      <div class="chat-header">
        <div class="chat-info">
          <el-button :icon="ArrowLeft" circle @click="backToConfig" />
          <div class="info-text">
            <h3>{{ currentConfig.subject }} • {{ currentConfig.grade }}</h3>
            <p>{{ currentConfig.province }}</p>
          </div>
        </div>
        <div class="chat-actions">
          <el-button :icon="Delete" @click="confirmClearChat">清空对话</el-button>
        </div>
      </div>

      <div ref="messagesContainerRef" class="messages-container">
        <div v-if="conversationStore.currentMessages.length === 0" class="empty-messages">
          <el-empty description="开始你的第一个问题吧" />
        </div>

        <MessageItem
          v-for="message in conversationStore.currentMessages"
          :key="message.id"
          :message="message"
        />

        <!-- 正在输入提示 -->
        <div v-if="isAITyping" class="ai-typing">
          <el-avatar :size="36" style="background-color: #409eff">
            <el-icon><Robot /></el-icon>
          </el-avatar>
          <div class="typing-indicator">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
      </div>

      <div class="input-panel">
        <QuestionInput
          :disabled="isAITyping"
          :loading="isAITyping"
          @send="handleSendMessage"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Setting,
  EditPen,
  Picture,
  Document,
  ChatDotRound,
  ArrowLeft,
  Delete,
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { useConversationStore } from '@/stores/conversation'
import { PROVINCES, GRADES, SUBJECTS, getProvinceName, getGradeName, getSubjectName } from '@/utils/constants'
import { sendMessage, sendMessagePost } from '@/api/conversation.mock'
import MessageItem from '@/components/MessageItem.vue'
import QuestionInput from '@/components/QuestionInput.vue'
import type { Message } from '@/types'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const conversationStore = useConversationStore()

const configForm = ref({
  provinceCode: userStore.userInfo?.provinceCode || '',
  grade: userStore.userInfo?.grade || '',
  subject: 'math',
})

const currentConversationId = ref<number | null>(null)
const isAITyping = ref(false)
const messagesContainerRef = ref<HTMLElement>()

const canStartChat = computed(() => {
  return configForm.value.provinceCode && configForm.value.grade && configForm.value.subject
})

const currentConfig = computed(() => {
  return {
    province: getProvinceName(configForm.value.provinceCode),
    grade: getGradeName(configForm.value.grade),
    subject: getSubjectName(configForm.value.subject),
  }
})

// 开始新对话
const startNewChat = async () => {
  try {
    const conversationId = await conversationStore.createConversation({
      provinceCode: configForm.value.provinceCode,
      grade: configForm.value.grade,
      subject: configForm.value.subject,
    })
    currentConversationId.value = conversationId
    conversationStore.setCurrentMessages([])
  } catch (error) {
    console.error('创建对话失败:', error)
    ElMessage.error('创建对话失败，请重试')
  }
}

// 返回配置面板
const backToConfig = () => {
  currentConversationId.value = null
  conversationStore.clearCurrentConversation()
}

// 确认清空对话
const confirmClearChat = async () => {
  try {
    await ElMessageBox.confirm('确定要清空当前对话吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    conversationStore.setCurrentMessages([])
    ElMessage.success('已清空对话')
  } catch (error) {
    // 用户取消
  }
}

// 发送消息
const handleSendMessage = async (data: { content: string; files: any[]; aiModel: string }) => {
  if (!currentConversationId.value) return

  // 添加用户消息到列表
  const userMessage: Message = {
    id: Date.now(),
    conversationId: currentConversationId.value,
    userId: userStore.userInfo!.id,
    role: 'user',
    contentType: data.files.length > 0 ? 'image' : 'text',
    content: data.content,
    files: data.files,
    createTime: new Date().toISOString(),
  }
  conversationStore.addMessage(userMessage)

  // 滚动到底部
  scrollToBottom()

  // 显示AI正在输入
  isAITyping.value = true

  try {
    // 使用流式API（如果支持）
    let aiContent = ''
    const aiMessage: Message = {
      id: Date.now() + 1,
      conversationId: currentConversationId.value,
      userId: userStore.userInfo!.id,
      role: 'assistant',
      contentType: 'text',
      content: '',
      aiModel: data.aiModel,
      createTime: new Date().toISOString(),
    }

    // 先添加空消息
    conversationStore.addMessage(aiMessage)

    // 使用普通POST方式（流式需要SSE支持）
    const response = await sendMessagePost(currentConversationId.value, {
      contentType: data.files.length > 0 ? 'image' : 'text',
      content: data.content,
      files: data.files,
      aiModel: data.aiModel,
    })

    // 更新AI消息
    const messages = conversationStore.currentMessages
    const lastMessage = messages[messages.length - 1]
    if (lastMessage.role === 'assistant') {
      lastMessage.content = response.content
      lastMessage.id = response.id
      lastMessage.tokens = response.tokens
    }

    scrollToBottom()
  } catch (error) {
    console.error('发送消息失败:', error)
    ElMessage.error('发送消息失败，请重试')
    // 移除最后一条AI消息
    const messages = conversationStore.currentMessages
    if (messages[messages.length - 1].role === 'assistant') {
      messages.pop()
    }
  } finally {
    isAITyping.value = false
  }
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainerRef.value) {
      messagesContainerRef.value.scrollTop = messagesContainerRef.value.scrollHeight
    }
  })
}

// 监听消息变化，自动滚动
watch(
  () => conversationStore.currentMessages.length,
  () => {
    scrollToBottom()
  }
)

onMounted(async () => {
  // 如果有conversationId参数，加载对话详情
  const conversationId = route.params.id
  if (conversationId && typeof conversationId === 'string') {
    try {
      currentConversationId.value = parseInt(conversationId)
      await conversationStore.fetchConversationDetail(currentConversationId.value)
    } catch (error) {
      console.error('加载对话详情失败:', error)
      ElMessage.error('加载对话详情失败')
      router.push('/chat')
    }
  }
})
</script>

<style scoped lang="scss">
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  max-width: 1200px;
  margin: 0 auto;
}

.config-panel {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 20px;

  .welcome-section {
    text-align: center;
    margin-bottom: 40px;

    .welcome-title {
      font-size: 32px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 12px;
    }

    .welcome-subtitle {
      font-size: 16px;
      color: #909399;
      line-height: 1.6;
    }
  }

  .config-card {
    width: 100%;
    max-width: 600px;
    margin-bottom: 40px;

    .card-header {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 16px;
      font-weight: 500;
    }

    .subject-radio-group {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;

      .subject-icon {
        margin-right: 4px;
      }
    }
  }

  .quick-tips {
    width: 100%;
    max-width: 600px;

    h3 {
      font-size: 18px;
      font-weight: 500;
      color: #303133;
      margin-bottom: 20px;
      text-align: center;
    }

    .tips-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
      gap: 16px;

      .tip-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 20px;
        background: #fff;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        text-align: center;

        .el-icon {
          font-size: 32px;
          color: #409eff;
          margin-bottom: 12px;
        }

        p {
          font-size: 14px;
          color: #606266;
          line-height: 1.6;
        }
      }
    }
  }
}

.chat-panel {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 108px);
  background: #fff;
  border-radius: 12px;
  overflow: hidden;

  .chat-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 16px 20px;
    border-bottom: 1px solid #e5e7eb;

    .chat-info {
      display: flex;
      align-items: center;
      gap: 12px;

      .info-text {
        h3 {
          font-size: 16px;
          font-weight: 500;
          color: #303133;
          margin-bottom: 4px;
        }

        p {
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }

  .messages-container {
    flex: 1;
    overflow-y: auto;
    padding: 20px;

    .empty-messages {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 100%;
    }

    .ai-typing {
      display: flex;
      gap: 12px;
      padding: 16px 0;

      .typing-indicator {
        display: flex;
        align-items: center;
        gap: 4px;
        padding: 12px 16px;
        background: #f5f7fa;
        border-radius: 12px;

        span {
          width: 8px;
          height: 8px;
          background: #909399;
          border-radius: 50%;
          animation: typing 1.4s infinite;

          &:nth-child(2) {
            animation-delay: 0.2s;
          }

          &:nth-child(3) {
            animation-delay: 0.4s;
          }
        }
      }
    }
  }

  .input-panel {
    padding: 20px;
    border-top: 1px solid #e5e7eb;
    background: #fff;
  }
}

@keyframes typing {
  0%,
  60%,
  100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-10px);
  }
}

// 响应式
@media (max-width: 768px) {
  .config-panel {
    .welcome-section {
      .welcome-title {
        font-size: 24px;
      }

      .welcome-subtitle {
        font-size: 14px;
      }
    }

    .quick-tips {
      .tips-grid {
        grid-template-columns: repeat(2, 1fr);
      }
    }
  }
}

// 深色模式
.dark {
  .config-panel {
    .welcome-section {
      .welcome-title {
        color: #e5e5e5;
      }
    }

    .quick-tips {
      h3 {
        color: #e5e5e5;
      }

      .tips-grid {
        .tip-item {
          background: #1a1a1a;

          p {
            color: #e5e5e5;
          }
        }
      }
    }
  }

  .chat-panel {
    background: #1a1a1a;

    .chat-header {
      border-bottom-color: #2a2a2a;

      .chat-info {
        .info-text {
          h3 {
            color: #e5e5e5;
          }
        }
      }
    }

    .messages-container {
      .ai-typing {
        .typing-indicator {
          background: #2a2a2a;
        }
      }
    }

    .input-panel {
      border-top-color: #2a2a2a;
      background: #1a1a1a;
    }
  }
}
</style>


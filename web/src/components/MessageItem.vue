<template>
  <div class="message-item" :class="{ user: message.role === 'user', assistant: message.role === 'assistant' }">
    <div class="message-avatar">
      <el-avatar v-if="message.role === 'user'" :src="userStore.userInfo?.avatar" :size="36">
        {{ userStore.userInfo?.nickname?.charAt(0) }}
      </el-avatar>
      <el-avatar v-else :size="36" style="background-color: #409eff">
        <el-icon><Robot /></el-icon>
      </el-avatar>
    </div>

    <div class="message-content">
      <div class="message-header">
        <span class="message-role">{{ message.role === 'user' ? '我' : 'AI教师' }}</span>
        <span class="message-time">{{ formatRelativeTime(message.createTime) }}</span>
      </div>

      <div class="message-body">
        <!-- 文本消息 -->
        <div v-if="message.contentType === 'text'" class="message-text">
          <div v-if="message.role === 'assistant'" class="markdown-body" v-html="renderedContent"></div>
          <div v-else>{{ message.content }}</div>
        </div>

        <!-- 图片消息 -->
        <div v-if="message.files && message.files.length > 0" class="message-files">
          <div v-for="file in message.files" :key="file.fileId" class="file-item">
            <el-image
              v-if="isImage(file.fileType)"
              :src="file.thumbnailUrl || file.fileUrl"
              :preview-src-list="[file.fileUrl]"
              fit="cover"
              class="file-image"
            />
            <div v-else class="file-card" @click="downloadFile(file)">
              <el-icon class="file-icon"><Document /></el-icon>
              <div class="file-info">
                <div class="file-name">{{ file.fileName }}</div>
                <div class="file-size">{{ formatFileSize(file.fileSize) }}</div>
              </div>
            </div>
          </div>
        </div>

        <!-- AI模型标识 -->
        <div v-if="message.role === 'assistant' && message.aiModel" class="message-meta">
          <el-tag size="small" type="info">{{ getAIModelName(message.aiModel) }}</el-tag>
          <span v-if="message.tokens" class="tokens">{{ message.tokens }} tokens</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { Message } from '@/types'
import { useUserStore } from '@/stores/user'
import { formatRelativeTime, formatFileSize } from '@/utils/format'
import { getAIModelName } from '@/utils/constants'
import { renderMarkdown } from '@/utils/markdown'

interface Props {
  message: Message
}

const props = defineProps<Props>()
const userStore = useUserStore()

const renderedContent = computed(() => {
  if (props.message.role === 'assistant' && props.message.contentType === 'text') {
    return renderMarkdown(props.message.content)
  }
  return props.message.content
})

const isImage = (fileType: string) => {
  return ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp'].includes(fileType.toLowerCase())
}

const downloadFile = (file: any) => {
  window.open(file.fileUrl, '_blank')
}
</script>

<style scoped lang="scss">
.message-item {
  display: flex;
  gap: 12px;
  padding: 16px 0;

  &.user {
    flex-direction: row-reverse;

    .message-content {
      align-items: flex-end;
    }

    .message-body {
      background: #409eff;
      color: #fff;
    }
  }

  .message-avatar {
    flex-shrink: 0;
  }

  .message-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    min-width: 0;

    .message-header {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 8px;
      font-size: 12px;

      .message-role {
        font-weight: 500;
        color: #303133;
      }

      .message-time {
        color: #909399;
      }
    }

    .message-body {
      display: inline-block;
      max-width: 80%;
      padding: 12px 16px;
      background: #f5f7fa;
      border-radius: 12px;
      word-break: break-word;

      .message-text {
        line-height: 1.6;
      }

      .message-files {
        display: flex;
        flex-wrap: wrap;
        gap: 12px;
        margin-top: 8px;

        .file-item {
          .file-image {
            width: 200px;
            height: 200px;
            border-radius: 8px;
            cursor: pointer;
          }

          .file-card {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 12px;
            background: #fff;
            border: 1px solid #e5e7eb;
            border-radius: 8px;
            cursor: pointer;
            transition: all 0.3s;

            &:hover {
              border-color: #409eff;
              background: #ecf5ff;
            }

            .file-icon {
              font-size: 32px;
              color: #409eff;
            }

            .file-info {
              .file-name {
                font-size: 14px;
                color: #303133;
                margin-bottom: 4px;
              }

              .file-size {
                font-size: 12px;
                color: #909399;
              }
            }
          }
        }
      }

      .message-meta {
        display: flex;
        align-items: center;
        gap: 8px;
        margin-top: 8px;
        font-size: 12px;
        color: #909399;

        .tokens {
          color: #909399;
        }
      }
    }
  }
}

// 深色模式
.dark {
  .message-item {
    .message-content {
      .message-header {
        .message-role {
          color: #e5e5e5;
        }
      }

      .message-body {
        background: #2a2a2a;
        color: #e5e5e5;

        .message-files {
          .file-card {
            background: #1a1a1a;
            border-color: #3a3a3a;

            &:hover {
              background: #2a3a4a;
              border-color: #409eff;
            }

            .file-info {
              .file-name {
                color: #e5e5e5;
              }
            }
          }
        }
      }
    }
  }

  .message-item.user {
    .message-body {
      background: #409eff;
      color: #fff;
    }
  }
}
</style>


<template>
  <div class="question-input">
    <!-- 文件预览 -->
    <div v-if="uploadedFiles.length > 0" class="file-preview">
      <div v-for="(file, index) in uploadedFiles" :key="file.fileId" class="file-preview-item">
        <img v-if="isImage(file.fileType)" :src="file.thumbnailUrl || file.fileUrl" alt="" class="preview-image" />
        <div v-else class="preview-file">
          <el-icon><Document /></el-icon>
          <span class="file-name">{{ file.fileName }}</span>
        </div>
        <el-icon class="remove-icon" @click="removeFile(index)"><CircleCloseFilled /></el-icon>
      </div>
    </div>

    <!-- 输入框 -->
    <div class="input-container">
      <el-input
        v-model="inputText"
        type="textarea"
        :rows="3"
        :placeholder="placeholder"
        :disabled="disabled"
        resize="none"
        @keydown.enter.exact="handleEnterKey"
      />

      <div class="input-actions">
        <div class="left-actions">
          <!-- 上传图片 -->
          <el-tooltip content="上传图片">
            <FileUpload
              accept="image/jpeg,image/jpg,image/png,image/gif,image/webp"
              :max-size="5"
              :multiple="true"
              :disabled="disabled"
              @success="handleFileUpload"
            >
              <el-button :icon="Picture" circle :disabled="disabled" />
            </FileUpload>
          </el-tooltip>

          <!-- 上传文件 -->
          <el-tooltip content="上传文件">
            <FileUpload
              accept=".pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx"
              :max-size="20"
              :disabled="disabled"
              @success="handleFileUpload"
            >
              <el-button :icon="Document" circle :disabled="disabled" />
            </FileUpload>
          </el-tooltip>

          <!-- AI模型选择 -->
          <el-select v-model="selectedModel" placeholder="选择AI模型" style="width: 140px" :disabled="disabled">
            <el-option
              v-for="model in AI_MODELS"
              :key="model.code"
              :label="model.name"
              :value="model.code"
            >
              <div class="model-option">
                <span>{{ model.name }}</span>
                <span class="model-provider">{{ model.provider }}</span>
              </div>
            </el-option>
          </el-select>
        </div>

        <div class="right-actions">
          <el-button type="primary" :icon="Promotion" :loading="loading" :disabled="disabled || !canSend" @click="handleSend">
            发送
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { Picture, Document, Promotion } from '@element-plus/icons-vue'
import type { FileItem } from '@/types'
import { AI_MODELS } from '@/utils/constants'
import FileUpload from './FileUpload.vue'

interface Props {
  placeholder?: string
  disabled?: boolean
  loading?: boolean
}

interface Emits {
  (e: 'send', data: { content: string; files: FileItem[]; aiModel: string }): void
}

const props = withDefaults(defineProps<Props>(), {
  placeholder: '请输入您的问题...',
  disabled: false,
  loading: false,
})

const emit = defineEmits<Emits>()

const inputText = ref('')
const uploadedFiles = ref<FileItem[]>([])
const selectedModel = ref(AI_MODELS[0].code)

const canSend = computed(() => {
  return inputText.value.trim() !== '' || uploadedFiles.value.length > 0
})

const isImage = (fileType: string) => {
  return ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp'].includes(fileType.toLowerCase())
}

const handleFileUpload = (file: FileItem) => {
  uploadedFiles.value.push(file)
}

const removeFile = (index: number) => {
  uploadedFiles.value.splice(index, 1)
}

const handleEnterKey = (e: KeyboardEvent) => {
  if (!e.shiftKey && !e.ctrlKey && !e.metaKey) {
    e.preventDefault()
    handleSend()
  }
}

const handleSend = () => {
  if (!canSend.value || props.disabled || props.loading) return

  emit('send', {
    content: inputText.value.trim(),
    files: uploadedFiles.value,
    aiModel: selectedModel.value,
  })

  // 清空输入
  inputText.value = ''
  uploadedFiles.value = []
}
</script>

<style scoped lang="scss">
.question-input {
  .file-preview {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
    margin-bottom: 12px;

    .file-preview-item {
      position: relative;
      width: 80px;
      height: 80px;
      border: 1px solid #e5e7eb;
      border-radius: 8px;
      overflow: hidden;

      .preview-image {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      .preview-file {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 100%;
        padding: 8px;
        background: #f5f7fa;

        .el-icon {
          font-size: 24px;
          color: #409eff;
          margin-bottom: 4px;
        }

        .file-name {
          font-size: 12px;
          color: #606266;
          text-align: center;
          word-break: break-all;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
        }
      }

      .remove-icon {
        position: absolute;
        top: 4px;
        right: 4px;
        font-size: 18px;
        color: #f56c6c;
        background: #fff;
        border-radius: 50%;
        cursor: pointer;
        transition: transform 0.3s;

        &:hover {
          transform: scale(1.2);
        }
      }
    }
  }

  .input-container {
    :deep(.el-textarea__inner) {
      border-radius: 12px;
      border-color: #e5e7eb;
      font-size: 14px;

      &:focus {
        border-color: #409eff;
      }
    }

    .input-actions {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-top: 12px;

      .left-actions {
        display: flex;
        align-items: center;
        gap: 8px;
      }

      .right-actions {
        display: flex;
        align-items: center;
        gap: 8px;
      }
    }
  }
}

.model-option {
  display: flex;
  align-items: center;
  justify-content: space-between;

  .model-provider {
    font-size: 12px;
    color: #909399;
  }
}

// 深色模式
.dark {
  .question-input {
    .file-preview {
      .file-preview-item {
        border-color: #3a3a3a;

        .preview-file {
          background: #2a2a2a;

          .file-name {
            color: #e5e5e5;
          }
        }
      }
    }

    .input-container {
      :deep(.el-textarea__inner) {
        background: #2a2a2a;
        border-color: #3a3a3a;
        color: #e5e5e5;
      }
    }
  }
}
</style>


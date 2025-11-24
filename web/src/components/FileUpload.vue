<template>
  <div class="file-upload">
    <el-upload
      ref="uploadRef"
      :action="uploadAction"
      :before-upload="handleBeforeUpload"
      :on-success="handleSuccess"
      :on-error="handleError"
      :on-progress="handleProgress"
      :show-file-list="false"
      :multiple="multiple"
      :accept="accept"
      :disabled="disabled"
    >
      <slot>
        <el-button :icon="Upload" :disabled="disabled">上传文件</el-button>
      </slot>
    </el-upload>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Upload } from '@element-plus/icons-vue'
import type { UploadInstance } from 'element-plus'
import type { FileItem } from '@/types'

interface Props {
  accept?: string
  maxSize?: number // MB
  multiple?: boolean
  disabled?: boolean
}

interface Emits {
  (e: 'success', file: FileItem): void
  (e: 'error', error: Error): void
  (e: 'progress', percent: number): void
}

const props = withDefaults(defineProps<Props>(), {
  accept: '*',
  maxSize: 20,
  multiple: false,
  disabled: false,
})

const emit = defineEmits<Emits>()

const uploadRef = ref<UploadInstance>()
const uploadAction = ref('/api/file/upload')

const handleBeforeUpload = (file: File) => {
  // 检查文件大小
  const isLtMaxSize = file.size / 1024 / 1024 < props.maxSize
  if (!isLtMaxSize) {
    ElMessage.error(`文件大小不能超过 ${props.maxSize}MB`)
    return false
  }
  return true
}

const handleSuccess = (response: any) => {
  if (response.code === 200) {
    emit('success', response.data)
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(response.message || '上传失败')
    emit('error', new Error(response.message))
  }
}

const handleError = (error: Error) => {
  console.error('上传失败:', error)
  ElMessage.error('上传失败，请重试')
  emit('error', error)
}

const handleProgress = (event: any) => {
  emit('progress', event.percent)
}
</script>

<style scoped lang="scss">
.file-upload {
  display: inline-block;
}
</style>


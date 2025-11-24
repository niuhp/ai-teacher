import { request } from '@/utils/request'
import type { FileItem } from '@/types'

// 获取上传签名
export const getUploadSignature = (params: {
  fileName: string
  fileType: string
}): Promise<{
  uploadUrl: string
  signature: string
  fileId: number
}> => {
  return request.get('/file/upload-signature', { params })
}

// 上传完成通知
export const notifyUploadComplete = (data: { fileId: number; fileUrl: string }): Promise<void> => {
  return request.post('/file/upload-complete', data)
}

// 文件处理（OCR、语音识别等）
export const processFile = (data: {
  fileId: number
  processType: 'ocr' | 'extract_text' | 'speech_to_text'
}): Promise<{ extractedText: string }> => {
  return request.post('/file/process', data)
}

// 直接上传文件
export const uploadFile = (file: File, onProgress?: (percent: number) => void): Promise<FileItem> => {
  return request.upload('/file/upload', file, onProgress)
}

// 删除文件
export const deleteFile = (fileId: number): Promise<void> => {
  return request.delete(`/file/${fileId}`)
}


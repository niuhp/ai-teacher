// 用户信息
export interface UserInfo {
  id: number
  openid: string
  nickname: string
  avatar: string
  gender: 0 | 1 | 2
  phone?: string
  provinceCode: string
  provinceName: string
  grade: string
  gradeName: string
  status: 0 | 1
  createTime: string
}

// 登录响应
export interface LoginResponse {
  token: string
  userInfo: UserInfo
}

// 二维码登录
export interface QrCodeLoginData {
  ticket: string
  qrcodeUrl: string
  expireTime: string
}

export enum QrCodeStatus {
  PENDING = 0,    // 待扫码
  SCANNED = 1,    // 已扫码
  CONFIRMED = 2,  // 已确认
  CANCELLED = 3,  // 已取消
  EXPIRED = 4     // 已过期
}

export interface QrCodeStatusResponse {
  status: QrCodeStatus
  token?: string
  userInfo?: UserInfo
}

// 对话相关
export interface Conversation {
  id: number
  userId: number
  title: string
  provinceCode: string
  grade: string
  subject: string
  status: 0 | 1
  isFavorite: 0 | 1
  createTime: string
  updateTime: string
  lastMessage?: string
}

export interface Message {
  id: number
  conversationId: number
  userId: number
  role: 'user' | 'assistant'
  contentType: 'text' | 'image' | 'file' | 'audio'
  content: string
  files?: FileItem[]
  aiModel?: string
  tokens?: number
  createTime: string
}

export interface FileItem {
  fileId: number
  fileName: string
  fileType: string
  fileSize: number
  fileUrl: string
  thumbnailUrl?: string
}

// 创建对话请求
export interface CreateConversationRequest {
  provinceCode: string
  grade: string
  subject: string
}

// 发送消息请求
export interface SendMessageRequest {
  contentType: 'text' | 'image' | 'file' | 'audio'
  content: string
  files?: FileItem[]
  aiModel?: string
}

// 教材相关
export interface Textbook {
  id: number
  name: string
  provinceCode: string
  grade: string
  subject: string
  version: string
  semester: 1 | 2
  coverUrl: string
  fileUrl: string
  status: 0 | 1
}

export interface RelatedKnowledge {
  textbookName: string
  chapter: string
  content: string
  similarity?: number
}

// 省份和年级
export interface Province {
  code: string
  name: string
}

export interface Grade {
  code: string
  name: string
  level: 'primary' | 'middle' | 'high'
}

export interface Subject {
  code: string
  name: string
  icon?: string
}

// AI模型
export interface AIModel {
  code: string
  name: string
  provider: string
  description?: string
}

// 分页响应
export interface PageResponse<T> {
  total: number
  list: T[]
  page?: number
  size?: number
}

// 通用API响应
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}


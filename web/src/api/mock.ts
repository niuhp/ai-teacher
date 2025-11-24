// Mock 数据 - 用于前端开发测试
// 正式环境请删除此文件，使用真实后端API

import type { 
  QrCodeLoginData, 
  QrCodeStatusResponse, 
  UserInfo,
  Conversation,
  Message,
  PageResponse,
} from '@/types'
import { QrCodeStatus } from '@/types'

// 模拟延迟
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms))

// Mock 用户信息
export const mockUserInfo: UserInfo = {
  id: 1,
  openid: 'mock_openid_123',
  nickname: '测试用户',
  avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
  gender: 1,
  phone: '13800138000',
  provinceCode: '11',
  provinceName: '北京市',
  grade: 'middle_1',
  gradeName: '初中一年级',
  status: 1,
  createTime: new Date().toISOString(),
}

// Mock 生成二维码
export const mockGenerateQrCode = async (): Promise<QrCodeLoginData> => {
  await delay(500)
  
  // 生成一个简单的二维码（使用占位图片）
  const ticket = `mock_ticket_${Date.now()}`
  
  return {
    ticket,
    qrcodeUrl: `https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=mock_login_${ticket}`,
    expireTime: new Date(Date.now() + 5 * 60 * 1000).toISOString(),
  }
}

// Mock 二维码状态（模拟扫码流程）
let mockScanCount = 0
export const mockGetQrCodeStatus = async (ticket: string): Promise<QrCodeStatusResponse> => {
  await delay(300)
  
  mockScanCount++
  
  // 模拟扫码流程：3次后变为已扫码，5次后自动确认
  if (mockScanCount >= 5) {
    mockScanCount = 0
    return {
      status: QrCodeStatus.CONFIRMED,
      token: 'mock_token_' + Date.now(),
      userInfo: mockUserInfo,
    }
  } else if (mockScanCount >= 3) {
    return {
      status: QrCodeStatus.SCANNED,
    }
  }
  
  return {
    status: QrCodeStatus.PENDING,
  }
}

// Mock 对话列表
export const mockConversationList: Conversation[] = [
  {
    id: 1,
    userId: 1,
    title: '二次方程解法',
    provinceCode: '11',
    grade: 'middle_1',
    subject: 'math',
    status: 1,
    isFavorite: 1,
    createTime: new Date(Date.now() - 86400000).toISOString(),
    updateTime: new Date(Date.now() - 3600000).toISOString(),
    lastMessage: '通过配方法可以将方程转化为...',
  },
  {
    id: 2,
    userId: 1,
    title: '英语语法问题',
    provinceCode: '11',
    grade: 'middle_1',
    subject: 'english',
    status: 1,
    isFavorite: 0,
    createTime: new Date(Date.now() - 172800000).toISOString(),
    updateTime: new Date(Date.now() - 7200000).toISOString(),
    lastMessage: '现在完成时表示过去发生的动作对现在造成的影响...',
  },
]

// Mock 消息列表
export const mockMessages: Message[] = [
  {
    id: 1,
    conversationId: 1,
    userId: 1,
    role: 'user',
    contentType: 'text',
    content: '请帮我解释一下二次方程的解法',
    createTime: new Date(Date.now() - 3600000).toISOString(),
  },
  {
    id: 2,
    conversationId: 1,
    userId: 1,
    role: 'assistant',
    contentType: 'text',
    content: `# 二次方程的解法

二次方程的一般形式为：$ax^2 + bx + c = 0$ (其中 $a \\neq 0$)

## 主要解法：

### 1. 配方法
将方程转化为 $(x + m)^2 = n$ 的形式

### 2. 公式法
使用求根公式：
$$x = \\frac{-b \\pm \\sqrt{b^2 - 4ac}}{2a}$$

### 3. 因式分解法
将方程分解为两个一次因式的乘积

## 示例

解方程：$x^2 - 5x + 6 = 0$

**因式分解：**
$(x - 2)(x - 3) = 0$

所以：$x_1 = 2, x_2 = 3$

你还有什么问题吗？`,
    aiModel: 'qwen',
    tokens: 256,
    createTime: new Date(Date.now() - 3500000).toISOString(),
  },
]

// Mock 创建对话
export const mockCreateConversation = async (data: any): Promise<{ conversationId: number }> => {
  await delay(500)
  return { conversationId: Math.floor(Math.random() * 10000) }
}

// Mock 发送消息
export const mockSendMessage = async (conversationId: number, data: any): Promise<Message> => {
  await delay(1000) // 模拟AI思考时间
  
  return {
    id: Date.now(),
    conversationId,
    userId: 1,
    role: 'assistant',
    contentType: 'text',
    content: `这是一个 Mock 回答。\n\n你的问题是："${data.content}"\n\n由于后端服务还没启动，这只是一个测试回复。\n\n真实的AI回答会更加详细和准确！`,
    aiModel: data.aiModel || 'qwen',
    tokens: 128,
    createTime: new Date().toISOString(),
  }
}

// Mock 获取对话列表
export const mockGetConversationList = async (params: any): Promise<PageResponse<Conversation>> => {
  await delay(300)
  
  return {
    total: mockConversationList.length,
    list: mockConversationList,
    page: params.page || 1,
    size: params.size || 20,
  }
}

// Mock 获取对话详情
export const mockGetConversationDetail = async (conversationId: number) => {
  await delay(300)
  
  return {
    id: conversationId,
    title: '二次方程解法',
    messages: mockMessages,
  }
}

// 是否启用 Mock（在开发环境且后端未启动时使用）
export const USE_MOCK = import.meta.env.DEV && import.meta.env.VITE_USE_MOCK === 'true'


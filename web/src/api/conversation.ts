import { request } from '@/utils/request'
import type {
  Conversation,
  Message,
  CreateConversationRequest,
  SendMessageRequest,
  PageResponse,
} from '@/types'

// 创建对话
export const createConversation = (data: CreateConversationRequest): Promise<{ conversationId: number }> => {
  return request.post('/conversation/create', data)
}

// 获取对话列表
export const getConversationList = (params: {
  page: number
  size: number
  subject?: string
}): Promise<PageResponse<Conversation>> => {
  return request.get('/conversation/list', { params })
}

// 获取对话详情
export const getConversationDetail = (conversationId: number): Promise<{
  id: number
  title: string
  messages: Message[]
}> => {
  return request.get(`/conversation/${conversationId}`)
}

// 发送消息（流式）
export const sendMessage = (
  conversationId: number,
  data: SendMessageRequest,
  onMessage: (chunk: string) => void,
  onComplete: () => void
): Promise<void> => {
  return new Promise((resolve, reject) => {
    const eventSource = new EventSource(
      `/api/conversation/${conversationId}/message?` +
        new URLSearchParams({
          contentType: data.contentType,
          content: data.content,
          files: JSON.stringify(data.files || []),
          aiModel: data.aiModel || '',
        }).toString()
    )

    eventSource.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data)
        if (data.content) {
          onMessage(data.content)
        }
      } catch (e) {
        console.error('Parse message error:', e)
      }
    }

    eventSource.addEventListener('complete', () => {
      eventSource.close()
      onComplete()
      resolve()
    })

    eventSource.onerror = (error) => {
      eventSource.close()
      reject(error)
    }
  })
}

// 发送消息（普通POST，备用方案）
export const sendMessagePost = (conversationId: number, data: SendMessageRequest): Promise<Message> => {
  return request.post(`/conversation/${conversationId}/message`, data)
}

// 删除对话
export const deleteConversation = (conversationId: number): Promise<void> => {
  return request.delete(`/conversation/${conversationId}`)
}

// 收藏/取消收藏对话
export const toggleFavorite = (conversationId: number, isFavorite: boolean): Promise<void> => {
  return request.put(`/conversation/${conversationId}/favorite`, { isFavorite })
}

// 更新对话标题
export const updateConversationTitle = (conversationId: number, title: string): Promise<void> => {
  return request.put(`/conversation/${conversationId}/title`, { title })
}


import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Conversation, Message } from '@/types'
import {
  getConversationList,
  getConversationDetail,
  createConversation as createConversationApi,
} from '@/api/conversation.mock'

export const useConversationStore = defineStore('conversation', () => {
  const currentConversation = ref<Conversation | null>(null)
  const currentMessages = ref<Message[]>([])
  const conversationList = ref<Conversation[]>([])
  const loading = ref(false)

  // 设置当前对话
  const setCurrentConversation = (conversation: Conversation | null) => {
    currentConversation.value = conversation
  }

  // 设置当前消息列表
  const setCurrentMessages = (messages: Message[]) => {
    currentMessages.value = messages
  }

  // 添加消息
  const addMessage = (message: Message) => {
    currentMessages.value.push(message)
  }

  // 创建对话
  const createConversation = async (data: {
    provinceCode: string
    grade: string
    subject: string
  }) => {
    try {
      loading.value = true
      const result = await createConversationApi(data)
      return result.conversationId
    } catch (error) {
      console.error('创建对话失败:', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  // 获取对话列表
  const fetchConversationList = async (params: {
    page: number
    size: number
    subject?: string
  }) => {
    try {
      loading.value = true
      const result = await getConversationList(params)
      conversationList.value = result.list
      return result
    } catch (error) {
      console.error('获取对话列表失败:', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  // 获取对话详情
  const fetchConversationDetail = async (conversationId: number) => {
    try {
      loading.value = true
      const result = await getConversationDetail(conversationId)
      currentMessages.value = result.messages
      return result
    } catch (error) {
      console.error('获取对话详情失败:', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  // 清空当前对话
  const clearCurrentConversation = () => {
    currentConversation.value = null
    currentMessages.value = []
  }

  return {
    currentConversation,
    currentMessages,
    conversationList,
    loading,
    setCurrentConversation,
    setCurrentMessages,
    addMessage,
    createConversation,
    fetchConversationList,
    fetchConversationDetail,
    clearCurrentConversation,
  }
})


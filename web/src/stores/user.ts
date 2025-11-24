import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { UserInfo } from '@/types'
import { getUserInfo, updateUserInfo } from '@/api/user'
import { logout as logoutApi } from '@/api/auth.mock'
import { storage } from '@/utils/storage'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(storage.get('token') || '')
  const userInfo = ref<UserInfo | null>(storage.get('userInfo'))

  // 设置token
  const setToken = (newToken: string) => {
    token.value = newToken
    storage.set('token', newToken)
  }

  // 设置用户信息
  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info
    storage.set('userInfo', info)
  }

  // 获取用户信息
  const fetchUserInfo = async () => {
    try {
      const info = await getUserInfo()
      setUserInfo(info)
      return info
    } catch (error) {
      console.error('获取用户信息失败:', error)
      throw error
    }
  }

  // 更新用户信息
  const updateUser = async (data: Partial<UserInfo>) => {
    try {
      await updateUserInfo(data)
      await fetchUserInfo()
    } catch (error) {
      console.error('更新用户信息失败:', error)
      throw error
    }
  }

  // 登录
  const login = (newToken: string, info: UserInfo) => {
    setToken(newToken)
    setUserInfo(info)
  }

  // 退出登录
  const logout = async () => {
    try {
      await logoutApi()
    } catch (error) {
      console.error('退出登录失败:', error)
    } finally {
      token.value = ''
      userInfo.value = null
      storage.remove('token')
      storage.remove('userInfo')
    }
  }

  return {
    token,
    userInfo,
    setToken,
    setUserInfo,
    fetchUserInfo,
    updateUser,
    login,
    logout,
  }
}, {
  persist: true
})


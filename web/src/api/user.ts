import { request } from '@/utils/request'
import type { UserInfo } from '@/types'

// 获取用户信息
export const getUserInfo = (): Promise<UserInfo> => {
  return request.get('/user/info')
}

// 更新用户信息
export const updateUserInfo = (data: Partial<UserInfo>): Promise<void> => {
  return request.put('/user/info', data)
}


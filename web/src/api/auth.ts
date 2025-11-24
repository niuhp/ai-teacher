import { request } from '@/utils/request'
import type { LoginResponse, QrCodeLoginData, QrCodeStatusResponse } from '@/types'

// 生成登录二维码
export const generateQrCode = (): Promise<QrCodeLoginData> => {
  return request.post('/auth/qrcode/generate')
}

// 轮询登录状态
export const getQrCodeStatus = (ticket: string): Promise<QrCodeStatusResponse> => {
  return request.get(`/auth/qrcode/status?ticket=${ticket}`)
}

// 扫码确认登录（小程序端调用）
export const confirmQrCodeLogin = (ticket: string, confirm: boolean): Promise<void> => {
  return request.post('/auth/qrcode/confirm', { ticket, confirm })
}

// 刷新token
export const refreshToken = (): Promise<{ token: string }> => {
  return request.post('/auth/refresh-token')
}

// 退出登录
export const logout = (): Promise<void> => {
  return request.post('/auth/logout')
}


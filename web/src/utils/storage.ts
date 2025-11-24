// 本地存储工具类
const STORAGE_PREFIX = 'ai_teacher_'

export const storage = {
  // 设置本地存储
  set(key: string, value: any): void {
    try {
      const data = JSON.stringify(value)
      localStorage.setItem(STORAGE_PREFIX + key, data)
    } catch (e) {
      console.error('Storage set error:', e)
    }
  },

  // 获取本地存储
  get<T = any>(key: string, defaultValue?: T): T | null {
    try {
      const data = localStorage.getItem(STORAGE_PREFIX + key)
      return data ? JSON.parse(data) : defaultValue || null
    } catch (e) {
      console.error('Storage get error:', e)
      return defaultValue || null
    }
  },

  // 删除本地存储
  remove(key: string): void {
    localStorage.removeItem(STORAGE_PREFIX + key)
  },

  // 清空本地存储
  clear(): void {
    const keys = Object.keys(localStorage)
    keys.forEach((key) => {
      if (key.startsWith(STORAGE_PREFIX)) {
        localStorage.removeItem(key)
      }
    })
  },
}

export const session = {
  // 设置会话存储
  set(key: string, value: any): void {
    try {
      const data = JSON.stringify(value)
      sessionStorage.setItem(STORAGE_PREFIX + key, data)
    } catch (e) {
      console.error('SessionStorage set error:', e)
    }
  },

  // 获取会话存储
  get<T = any>(key: string, defaultValue?: T): T | null {
    try {
      const data = sessionStorage.getItem(STORAGE_PREFIX + key)
      return data ? JSON.parse(data) : defaultValue || null
    } catch (e) {
      console.error('SessionStorage get error:', e)
      return defaultValue || null
    }
  },

  // 删除会话存储
  remove(key: string): void {
    sessionStorage.removeItem(STORAGE_PREFIX + key)
  },

  // 清空会话存储
  clear(): void {
    const keys = Object.keys(sessionStorage)
    keys.forEach((key) => {
      if (key.startsWith(STORAGE_PREFIX)) {
        sessionStorage.removeItem(key)
      }
    })
  },
}


import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/zh-cn'

dayjs.extend(relativeTime)
dayjs.locale('zh-cn')

// 格式化日期时间
export const formatDateTime = (date: string | Date, format = 'YYYY-MM-DD HH:mm:ss'): string => {
  return dayjs(date).format(format)
}

// 格式化相对时间
export const formatRelativeTime = (date: string | Date): string => {
  return dayjs(date).fromNow()
}

// 格式化文件大小
export const formatFileSize = (bytes: number): string => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i]
}

// 格式化数字（千分位）
export const formatNumber = (num: number): string => {
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

// 截取字符串
export const truncate = (str: string, length: number, suffix = '...'): string => {
  if (str.length <= length) return str
  return str.substring(0, length) + suffix
}

// 高亮关键词
export const highlightKeyword = (text: string, keyword: string): string => {
  if (!keyword) return text
  const reg = new RegExp(keyword, 'gi')
  return text.replace(reg, (match) => `<mark>${match}</mark>`)
}


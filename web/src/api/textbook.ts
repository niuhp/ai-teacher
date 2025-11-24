import { request } from '@/utils/request'
import type { Textbook, RelatedKnowledge } from '@/types'

// 获取教材列表
export const getTextbookList = (params: {
  provinceCode: string
  grade: string
  subject: string
}): Promise<Textbook[]> => {
  return request.get('/textbook/list', { params })
}

// 搜索教材内容
export const searchTextbookContent = (data: {
  query: string
  provinceCode: string
  grade: string
  subject: string
}): Promise<RelatedKnowledge[]> => {
  return request.post('/textbook/search', data)
}


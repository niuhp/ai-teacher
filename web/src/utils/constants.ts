import type { Province, Grade, Subject, AIModel } from '@/types'

// 省份列表
export const PROVINCES: Province[] = [
  { code: '11', name: '北京市' },
  { code: '12', name: '天津市' },
  { code: '13', name: '河北省' },
  { code: '14', name: '山西省' },
  { code: '15', name: '内蒙古自治区' },
  { code: '21', name: '辽宁省' },
  { code: '22', name: '吉林省' },
  { code: '23', name: '黑龙江省' },
  { code: '31', name: '上海市' },
  { code: '32', name: '江苏省' },
  { code: '33', name: '浙江省' },
  { code: '34', name: '安徽省' },
  { code: '35', name: '福建省' },
  { code: '36', name: '江西省' },
  { code: '37', name: '山东省' },
  { code: '41', name: '河南省' },
  { code: '42', name: '湖北省' },
  { code: '43', name: '湖南省' },
  { code: '44', name: '广东省' },
  { code: '45', name: '广西壮族自治区' },
  { code: '46', name: '海南省' },
  { code: '50', name: '重庆市' },
  { code: '51', name: '四川省' },
  { code: '52', name: '贵州省' },
  { code: '53', name: '云南省' },
  { code: '54', name: '西藏自治区' },
  { code: '61', name: '陕西省' },
  { code: '62', name: '甘肃省' },
  { code: '63', name: '青海省' },
  { code: '64', name: '宁夏回族自治区' },
  { code: '65', name: '新疆维吾尔自治区' },
]

// 年级列表
export const GRADES: Grade[] = [
  { code: 'primary_1', name: '小学一年级', level: 'primary' },
  { code: 'primary_2', name: '小学二年级', level: 'primary' },
  { code: 'primary_3', name: '小学三年级', level: 'primary' },
  { code: 'primary_4', name: '小学四年级', level: 'primary' },
  { code: 'primary_5', name: '小学五年级', level: 'primary' },
  { code: 'primary_6', name: '小学六年级', level: 'primary' },
  { code: 'middle_1', name: '初中一年级', level: 'middle' },
  { code: 'middle_2', name: '初中二年级', level: 'middle' },
  { code: 'middle_3', name: '初中三年级', level: 'middle' },
  { code: 'high_1', name: '高中一年级', level: 'high' },
  { code: 'high_2', name: '高中二年级', level: 'high' },
  { code: 'high_3', name: '高中三年级', level: 'high' },
]

// 学科列表
export const SUBJECTS: Subject[] = [
  { code: 'chinese', name: '语文', icon: '📖' },
  { code: 'math', name: '数学', icon: '🔢' },
  { code: 'english', name: '英语', icon: '🔤' },
  { code: 'physics', name: '物理', icon: '⚛️' },
  { code: 'chemistry', name: '化学', icon: '🧪' },
  { code: 'biology', name: '生物', icon: '🧬' },
  { code: 'history', name: '历史', icon: '📜' },
  { code: 'geography', name: '地理', icon: '🌍' },
  { code: 'politics', name: '政治', icon: '⚖️' },
]

// AI模型列表
export const AI_MODELS: AIModel[] = [
  {
    code: 'qwen',
    name: '通义千问',
    provider: '阿里云',
    description: '擅长中文理解和生成',
  },
  {
    code: 'hunyuan',
    name: '腾讯元宝',
    provider: '腾讯云',
    description: '综合性能优秀',
  },
  {
    code: 'doubao',
    name: '豆包',
    provider: '字节跳动',
    description: '对话流畅自然',
  },
  {
    code: 'deepseek',
    name: 'DeepSeek',
    provider: 'DeepSeek',
    description: '理科推理能力强',
  },
]

// 工具函数：根据code获取名称
export const getProvinceName = (code: string): string => {
  return PROVINCES.find((p) => p.code === code)?.name || code
}

export const getGradeName = (code: string): string => {
  return GRADES.find((g) => g.code === code)?.name || code
}

export const getSubjectName = (code: string): string => {
  return SUBJECTS.find((s) => s.code === code)?.name || code
}

export const getAIModelName = (code: string): string => {
  return AI_MODELS.find((m) => m.code === code)?.name || code
}


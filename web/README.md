# AI教师 - Web端

基于 Vue 3 + TypeScript + Vite + Element Plus 构建的智能学习助手Web应用。

## 技术栈

- **框架**: Vue 3 (Composition API)
- **语言**: TypeScript
- **构建工具**: Vite
- **UI组件库**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP客户端**: Axios
- **Markdown渲染**: markdown-it
- **数学公式**: KaTeX
- **二维码**: qrcode

## 项目结构

```
web/
├── public/              # 静态资源
├── src/
│   ├── api/            # API接口
│   ├── assets/         # 资源文件
│   ├── components/     # 通用组件
│   ├── layouts/        # 布局组件
│   ├── router/         # 路由配置
│   ├── stores/         # 状态管理
│   ├── styles/         # 全局样式
│   ├── types/          # TypeScript类型定义
│   ├── utils/          # 工具函数
│   ├── views/          # 页面组件
│   ├── App.vue         # 根组件
│   └── main.ts         # 入口文件
├── index.html
├── package.json
├── tsconfig.json
└── vite.config.ts
```

## 开发

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build

# 预览生产构建
npm run preview
```

## 功能特性

- ✅ 微信扫码登录
- ✅ 多模态问答（文本、图片、文件、语音）
- ✅ 流式AI回答
- ✅ 对话历史管理
- ✅ 年级和省份选择
- ✅ Markdown + LaTeX公式渲染
- ✅ 响应式设计
- ✅ 深色模式（可选）

## 浏览器支持

- Chrome >= 90
- Firefox >= 88
- Safari >= 14
- Edge >= 90


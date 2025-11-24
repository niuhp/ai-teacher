# 🎭 Mock 数据使用说明

## ✅ 问题已解决！

### 为什么会"生成二维码失败"？

这是**正常现象**！因为：
- ✅ 前端代码已完成，可以正常运行
- ❌ 后端服务还没有启动
- 💡 前端尝试调用后端API `/api/auth/qrcode/generate`，但后端不存在

### 解决方案：Mock 数据

我已经为您配置了 **Mock 数据模式**，可以在没有后端的情况下测试所有前端功能！

## 🚀 如何使用

### 方法 1: 启用 Mock 模式（推荐）

已经在 `.env.development` 中配置好了：

```bash
VITE_USE_MOCK=true  # 使用 Mock 数据
```

**重启开发服务器**：

```bash
# 停止当前服务器 (Ctrl+C)
cd /Users/niuhaipeng/code-niuhp/ai-teacher/web
npm run dev
```

刷新浏览器，现在：
- ✅ 二维码可以正常生成（使用占位图片）
- ✅ 模拟扫码流程（10秒后自动登录）
- ✅ 可以正常创建对话
- ✅ 可以发送消息并收到Mock回复
- ✅ 可以查看历史记录

### 方法 2: 手动设置 Token（快速测试）

在浏览器控制台（F12 → Console）输入：

```javascript
localStorage.setItem('ai_teacher_token', JSON.stringify('test-token-12345'))
location.reload()
```

这样可以直接跳过登录，进入主界面。

## 🎬 Mock 数据功能演示

### 1. 扫码登录流程（自动化）

- 访问 http://localhost:3000/login
- 二维码自动生成（占位图片）
- **等待 10 秒**，系统会自动模拟：
  1. 第 6 秒：显示"已扫码"状态
  2. 第 10 秒：自动登录成功
- 自动跳转到主页面

### 2. 对话功能

1. **选择配置**：省份、年级、学科
2. **点击"开始对话"**：创建新会话（使用 Mock）
3. **输入问题**：例如"什么是二次方程？"
4. **查看回复**：
   - Mock 会返回一个测试回复
   - 支持 Markdown 和 LaTeX 公式渲染
   - 显示 AI 模型标识

### 3. 历史记录

- 进入"对话历史"页面
- 可以看到 2 条预设的历史对话
- 可以点击查看、收藏、删除

### 4. 个人中心

- 显示 Mock 用户信息
- 可以修改信息（只在前端生效）

## 📋 Mock 数据内容

### Mock 用户信息
```typescript
{
  nickname: '测试用户',
  provinceCode: '11',
  provinceName: '北京市',
  grade: 'middle_1',
  gradeName: '初中一年级',
}
```

### Mock 对话列表
- "二次方程解法"（数学，已收藏）
- "英语语法问题"（英语）

### Mock AI 回复
支持：
- Markdown 格式
- LaTeX 数学公式：$x^2 + 2x + 1 = 0$
- 代码高亮
- 分步骤讲解

## 🔧 配置说明

### 启用/禁用 Mock

编辑 `.env.development` 文件：

```bash
# 启用 Mock（后端未启动时使用）
VITE_USE_MOCK=true

# 禁用 Mock（后端已启动时使用）
VITE_USE_MOCK=false
```

修改后**必须重启开发服务器**！

### Mock 文件位置

```
src/api/
├── mock.ts              # Mock 数据定义
├── auth.mock.ts         # 认证 API (支持 Mock)
└── conversation.mock.ts # 对话 API (支持 Mock)
```

### 自定义 Mock 数据

编辑 `src/api/mock.ts`：

```typescript
// 修改用户信息
export const mockUserInfo: UserInfo = {
  nickname: '你的名字',  // 改成你想要的昵称
  grade: 'high_1',       // 改成你的年级
  // ... 其他字段
}

// 添加更多对话记录
export const mockConversationList: Conversation[] = [
  // 添加你的对话...
]
```

## 🎯 扫码登录自动化流程

当前配置：
- **第 0-6 秒**：显示"待扫码"状态
- **第 6-10 秒**：显示"已扫码，请在手机上确认"
- **第 10 秒**：自动登录成功

修改时间可以编辑 `src/api/mock.ts`：

```typescript
export const mockGetQrCodeStatus = async (ticket: string) => {
  mockScanCount++
  
  if (mockScanCount >= 5) {  // 改这个数字（5次 × 2秒 = 10秒）
    // 登录成功
  } else if (mockScanCount >= 3) {  // 改这个数字（3次 × 2秒 = 6秒）
    // 已扫码
  }
  // 待扫码
}
```

## 🔄 切换到真实后端

当后端服务启动后：

### 1. 修改环境变量

```bash
# .env.development
VITE_USE_MOCK=false
```

### 2. 确保后端地址正确

```bash
# vite.config.ts
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',  // 确保后端端口正确
      changeOrigin: true,
    },
  },
}
```

### 3. 重启开发服务器

```bash
npm run dev
```

### 4. 更新导入路径

将所有 `.mock.ts` 改回 `.ts`：

```typescript
// 改回原始导入
import { generateQrCode } from '@/api/auth'  // 去掉 .mock
import { sendMessagePost } from '@/api/conversation'  // 去掉 .mock
```

或者保留 `.mock.ts`，在文件内部控制是否使用 Mock。

## 📊 开发建议

### 当前阶段（无后端）
✅ 使用 `VITE_USE_MOCK=true`
- 测试前端所有功能
- 调整样式和交互
- 验证用户体验

### 后端开发中
✅ 使用 `VITE_USE_MOCK=false`
- 真实 API 调用
- 测试前后端集成
- 修复对接问题

### 生产环境
❌ 必须 `VITE_USE_MOCK=false`
- 使用真实数据
- 连接生产后端

## 🎉 现在可以做什么

1. ✅ **测试登录流程**
   - 访问 `/login`
   - 等待 10 秒自动登录

2. ✅ **测试对话功能**
   - 选择省份、年级、学科
   - 创建对话
   - 发送消息
   - 查看 AI 回复（带公式渲染）

3. ✅ **测试历史记录**
   - 查看对话列表
   - 筛选学科
   - 收藏/删除对话

4. ✅ **测试个人中心**
   - 查看用户信息
   - 修改设置
   - 切换深色模式

5. ✅ **测试响应式**
   - 调整浏览器窗口大小
   - 使用手机模式（F12 → 切换设备）

## 💡 提示

- 📝 所有 Mock 操作都会在控制台输出 `🔧 使用 Mock 数据：xxx`
- 🔄 Mock 数据不会真实保存，刷新页面后恢复初始状态
- 🎨 可以自由修改 Mock 数据测试各种场景
- ⚡ Mock 模式下响应速度很快（模拟了网络延迟）

## 🆘 遇到问题？

### 问题 1: Mock 没有生效
- 确认 `.env.development` 中 `VITE_USE_MOCK=true`
- **重启开发服务器**（修改环境变量必须重启）
- 清除浏览器缓存：`Ctrl+Shift+R`

### 问题 2: 控制台没有 Mock 提示
- 打开控制台（F12）查看
- 应该看到 `🔧 使用 Mock 数据：xxx`
- 如果没有，检查环境变量

### 问题 3: 自动登录不工作
- 确保等待足够时间（10秒）
- 检查控制台是否有错误
- 刷新页面重试

---

**祝开发愉快！** 🚀

现在你可以完整体验所有前端功能了！


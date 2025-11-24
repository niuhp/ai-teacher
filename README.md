# 🎓 AI教师 - 智能学习助手系统

一个基于AI大模型的中小学生学习辅导系统，支持多模态提问（文字、图片、语音、文件），覆盖全国各省份各年级教材知识点。

## 📚 项目简介

AI教师系统旨在为全国中小学生提供智能化的学习辅导服务，通过整合各年级电子教材和大语言模型技术，为学生提供精准、及时的问题解答服务。

### 核心特性

- 🎯 **多模态交互**: 支持文字、图片、语音、文件等多种提问方式
- 📖 **教材同步**: 覆盖全国31个省市、小学到高中各年级教材
- 🤖 **多AI模型**: 支持通义千问、腾讯元宝、豆包、DeepSeek等
- 📱 **多端支持**: 微信小程序 + Web页面
- 💡 **智能解答**: 基于RAG技术的精准知识检索和生成

## 🏗️ 技术架构

### 前端技术栈
- **Web端**: Vue 3 + TypeScript + Vite + Element Plus
- **小程序**: 微信小程序原生/uni-app
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP**: Axios

### 后端技术栈
- **框架**: Spring Boot 3.2.0
- **语言**: Java 17
- **ORM**: MyBatis-Plus 3.5.5
- **数据库**: MySQL 8.0+
- **缓存**: Redis
- **认证**: JWT
- **文件存储**: 阿里云OSS

## 📂 项目结构

```
ai-teacher/
├── web/                    # Web前端项目（Vue 3）
│   ├── src/
│   │   ├── api/           # API接口
│   │   ├── components/    # 通用组件
│   │   ├── views/         # 页面组件
│   │   ├── stores/        # 状态管理
│   │   ├── router/        # 路由配置
│   │   └── utils/         # 工具函数
│   └── package.json
│
├── server/                 # 后端服务（Spring Boot）
│   ├── src/main/
│   │   ├── java/
│   │   │   └── com/aiteacher/server/
│   │   │       ├── controller/    # 控制器
│   │   │       ├── service/       # 服务层
│   │   │       ├── mapper/        # 数据访问层
│   │   │       ├── entity/        # 实体类
│   │   │       ├── config/        # 配置类
│   │   │       └── util/          # 工具类
│   │   └── resources/
│   │       ├── application.yml    # 配置文件
│   │       └── db/schema.sql      # 数据库脚本
│   └── pom.xml
│
├── miniapp/                # 微信小程序（待开发）
│
├── 需求文档.md             # 详细需求文档
├── 后端启动指南.md         # 后端快速启动指南
└── README.md              # 本文件
```

## 🚀 快速开始

### 前端（Web）

```bash
# 1. 进入前端目录
cd web

# 2. 安装依赖
npm install

# 3. 启动开发服务器
npm run dev

# 4. 访问 http://localhost:3000
```

**详细说明**: 查看 `web/README.md` 或 `web/开发指南.md`

### 后端（Server）

```bash
# 1. 创建数据库
mysql -u root -p < server/src/main/resources/db/schema.sql

# 2. 修改配置
# 编辑 server/src/main/resources/application.yml
# 修改数据库密码

# 3. 启动项目
cd server
mvn spring-boot:run

# 4. 访问 http://localhost:8080/api
```

**详细说明**: 查看 `server/README.md` 或 `server/快速启动指南.md`

## 📖 文档说明

| 文档名称 | 说明 |
|---------|------|
| `需求文档.md` | 完整的系统需求文档，包含技术选型、功能设计、数据库设计等 |
| `后端开发进度总结.md` | 后端开发进度和待办事项 |
| `后端启动指南.md` | 后端详细的启动和测试指南 |
| `server/README.md` | 后端项目说明文档 |
| `web/README.md` | 前端项目说明文档 |
| `web/开发指南.md` | 前端详细开发指南 |
| `web/Mock数据使用说明.md` | 前端Mock数据使用方法 |

## ✅ 当前进度

### 前端进度：90%
- ✅ 项目架构搭建
- ✅ 登录页面（扫码登录）
- ✅ 对话页面（多模态输入）
- ✅ 历史记录页面
- ✅ 个人中心页面
- ✅ Mock数据支持
- ⏳ 空白页问题待调试

### 后端进度：90%
- ✅ 项目架构搭建
- ✅ 数据库设计
- ✅ 用户认证模块
- ✅ 扫码登录功能
- ✅ 对话管理模块
- ✅ 消息管理模块
- ⏳ AI模型接入（可选）
- ⏳ 文件上传（可选）

### 小程序进度：0%
- ⏳ 待开发

## 🎯 MVP功能清单

### 已实现
- ✅ 微信扫码登录（Mock）
- ✅ 用户信息管理
- ✅ 创建对话
- ✅ 发送消息（Mock AI回复）
- ✅ 对话历史查询
- ✅ 收藏对话
- ✅ Markdown + LaTeX渲染

### 待实现
- ⏳ 真实AI模型接入
- ⏳ 文件上传和处理
- ⏳ 教材管理
- ⏳ 语音输入
- ⏳ 微信小程序端

## 📡 主要API接口

### 认证相关
- `POST /api/auth/qrcode/generate` - 生成登录二维码
- `GET /api/auth/qrcode/status` - 查询登录状态
- `POST /api/auth/logout` - 退出登录

### 用户相关
- `GET /api/user/info` - 获取用户信息
- `PUT /api/user/info` - 更新用户信息

### 对话相关
- `POST /api/conversation/create` - 创建对话
- `GET /api/conversation/list` - 获取对话列表
- `POST /api/conversation/{id}/message` - 发送消息
- `GET /api/conversation/{id}` - 获取对话详情
- `DELETE /api/conversation/{id}` - 删除对话

## 🔧 配置说明

### 前端配置

```bash
# web/.env.development
VITE_API_BASE_URL=/api
VITE_USE_MOCK=true  # 是否使用Mock数据
```

### 后端配置

```yaml
# server/src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_teacher
    username: root
    password: your-password

ai-teacher:
  jwt:
    secret: your-jwt-secret
    expiration: 604800
  ai:
    qwen:
      api-key: your-api-key
```

## 🐛 常见问题

### 前端空白页
- 检查浏览器控制台错误
- 清除浏览器缓存
- 确认开发服务器正常运行

### 后端启动失败
- 检查MySQL是否启动
- 确认数据库已创建
- 检查端口8080是否被占用

### 跨域问题
- 后端已配置CORS
- 前端已配置代理
- 确认两个服务都在运行

## 📝 开发规范

### Git提交规范
```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 重构
perf: 性能优化
test: 测试相关
chore: 构建/工具相关
```

### 代码规范
- 前端：ESLint + Prettier
- 后端：阿里巴巴Java开发规范
- 统一使用UTF-8编码
- 合理的注释和文档

## 🚢 部署说明

### 前端部署
```bash
cd web
npm run build
# 将 dist 目录部署到服务器
```

### 后端部署
```bash
cd server
mvn clean package -DskipTests
java -jar target/ai-teacher-server-1.0.0.jar --spring.profiles.active=prod
```

## 📈 未来规划

### 短期目标
- [ ] 完善前后端联调
- [ ] 接入真实AI模型
- [ ] 实现文件上传功能
- [ ] 开发微信小程序端

### 长期目标
- [ ] 教材知识库建设
- [ ] RAG技术优化
- [ ] 用户学习数据分析
- [ ] 个性化学习推荐
- [ ] 多语言支持

## 🤝 贡献指南

欢迎贡献代码、提出问题或建议！

## 📄 许可证

MIT License

## 👥 联系方式

如有问题或建议，请联系开发团队。

---

**最后更新**: 2025-11-21  
**版本**: 1.0.0-MVP  
**状态**: 开发中 🚧


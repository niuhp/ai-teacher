# AI教师系统 - 后端服务

基于 Spring Boot 3 + Java 17 + MyBatis-Plus 构建的智能学习助手后端服务。

## 📦 技术栈

- **框架**: Spring Boot 3.2.0
- **语言**: Java 17
- **ORM**: MyBatis-Plus 3.5.5
- **数据库**: MySQL 8.0+
- **缓存**: Redis
- **认证**: JWT
- **文件存储**: 阿里云 OSS
- **构建工具**: Maven

## 🚀 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 5.0+

### 2. 创建数据库

```bash
# 连接 MySQL
mysql -u root -p

# 执行建表SQL
mysql> source /Users/niuhaipeng/code-niuhp/ai-teacher/server/src/main/resources/db/schema.sql
```

或者手动执行 `src/main/resources/db/schema.sql` 中的SQL语句。

### 3. 配置文件

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_teacher?...
    username: root
    password: your-password  # 修改为你的密码
  
  data:
    redis:
      host: localhost
      port: 6379
      password:  # 如果有密码，填写这里
```

### 4. 配置 AI 模型密钥

编辑 `application.yml` 中的 AI 配置：

```yaml
ai-teacher:
  ai:
    qwen:
      api-key: your-qwen-api-key
    hunyuan:
      app-id: your-hunyuan-appid
      secret-key: your-secret-key
    deepseek:
      api-key: your-deepseek-api-key
```

### 5. 启动项目

```bash
# 方式1: 使用 Maven
cd /Users/niuhaipeng/code-niuhp/ai-teacher/server
mvn spring-boot:run

# 方式2: 打包后运行
mvn clean package
java -jar target/ai-teacher-server-1.0.0.jar

# 方式3: 使用 IDE
# 直接运行 AiTeacherApplication.java
```

启动成功后，访问：http://localhost:8080/api

## 📁 项目结构

```
server/
├── src/main/
│   ├── java/com/aiteacher/server/
│   │   ├── AiTeacherApplication.java      # 启动类
│   │   ├── common/                         # 公共类
│   │   │   ├── Result.java                # 统一响应
│   │   │   ├── ResultCode.java            # 状态码
│   │   │   └── PageResult.java            # 分页响应
│   │   ├── config/                         # 配置类
│   │   │   ├── CorsConfig.java            # 跨域配置
│   │   │   ├── MyBatisPlusConfig.java     # MyBatis配置
│   │   │   └── RedisConfig.java           # Redis配置
│   │   ├── controller/                     # 控制器
│   │   │   ├── AuthController.java        # 认证接口
│   │   │   ├── UserController.java        # 用户接口
│   │   │   ├── ConversationController.java # 对话接口
│   │   │   └── FileController.java        # 文件接口
│   │   ├── service/                        # 服务层
│   │   │   ├── UserService.java
│   │   │   ├── ConversationService.java
│   │   │   ├── MessageService.java
│   │   │   ├── FileService.java
│   │   │   └── ai/                         # AI服务
│   │   │       ├── AIModelService.java    # AI接口
│   │   │       ├── QwenService.java       # 通义千问
│   │   │       ├── HunyuanService.java    # 腾讯元宝
│   │   │       └── DeepSeekService.java   # DeepSeek
│   │   ├── mapper/                         # Mapper接口
│   │   │   ├── UserMapper.java
│   │   │   ├── ConversationMapper.java
│   │   │   ├── MessageMapper.java
│   │   │   └── QrCodeLoginMapper.java
│   │   ├── entity/                         # 实体类
│   │   │   ├── User.java
│   │   │   ├── Conversation.java
│   │   │   ├── Message.java
│   │   │   └── QrCodeLogin.java
│   │   ├── dto/                            # 数据传输对象
│   │   ├── vo/                             # 视图对象
│   │   ├── exception/                      # 异常处理
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── BusinessException.java
│   │   └── utils/                          # 工具类
│   │       ├── JwtUtil.java               # JWT工具
│   │       ├── RedisUtil.java             # Redis工具
│   │       └── QrCodeUtil.java            # 二维码工具
│   └── resources/
│       ├── application.yml                 # 主配置
│       ├── application-dev.yml             # 开发环境
│       ├── application-prod.yml            # 生产环境
│       ├── db/
│       │   └── schema.sql                 # 建表SQL
│       └── mapper/                         # Mapper XML
├── pom.xml                                 # Maven配置
└── README.md                               # 本文档
```

## 🔧 配置说明

### 数据库配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_teacher
    username: root
    password: your-password
```

### Redis 配置

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password:
      database: 0
```

### JWT 配置

```yaml
ai-teacher:
  jwt:
    secret: aiteacher-secret-key-2025-very-long-and-secure
    expiration: 604800  # 7天
```

### 微信小程序配置

```yaml
ai-teacher:
  wechat:
    miniapp:
      app-id: your-miniapp-appid
      secret: your-miniapp-secret
```

### AI 模型配置

目前支持：
- 阿里通义千问 (Qwen)
- 腾讯元宝 (Hunyuan)
- 字节豆包 (Doubao)
- DeepSeek

## 📡 API 文档

### 基础响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1700000000000
}
```

### 认证接口

#### 1. 生成登录二维码
```
POST /api/auth/qrcode/generate
Response: {
  "ticket": "...",
  "qrcodeUrl": "...",
  "expireTime": "..."
}
```

#### 2. 查询登录状态
```
GET /api/auth/qrcode/status?ticket=xxx
Response: {
  "status": 2,
  "token": "...",
  "userInfo": {...}
}
```

### 用户接口

#### 1. 获取用户信息
```
GET /api/user/info
Headers: Authorization: Bearer {token}
```

#### 2. 更新用户信息
```
PUT /api/user/info
Headers: Authorization: Bearer {token}
Body: {...}
```

### 对话接口

#### 1. 创建对话
```
POST /api/conversation/create
Headers: Authorization: Bearer {token}
Body: {
  "provinceCode": "11",
  "grade": "middle_1",
  "subject": "math"
}
```

#### 2. 发送消息
```
POST /api/conversation/{id}/message
Headers: Authorization: Bearer {token}
Body: {
  "content": "...",
  "contentType": "text",
  "aiModel": "qwen"
}
```

#### 3. 获取对话列表
```
GET /api/conversation/list?page=1&size=20
Headers: Authorization: Bearer {token}
```

## 🔐 安全配置

### JWT Token

- Token 存储在 Header: `Authorization: Bearer {token}`
- Token 有效期：7天（可配置）
- Token 刷新机制已实现

### 跨域配置

已配置 CORS，允许前端访问。开发环境允许 `http://localhost:3000`。

### 敏感信息加密

- 密码使用 BCrypt 加密
- 敏感配置使用环境变量

## 🧪 测试

```bash
# 运行测试
mvn test

# 运行特定测试
mvn test -Dtest=UserServiceTest
```

## 📊 监控

### Druid 监控

访问：http://localhost:8080/api/druid

默认账号：admin / admin

### 应用监控

TODO: 集成 Spring Boot Actuator + Prometheus

## 🐛 常见问题

### 1. 数据库连接失败

检查：
- MySQL 是否启动
- 数据库名称、用户名、密码是否正确
- MySQL 8.0 需要配置 `allowPublicKeyRetrieval=true`

### 2. Redis 连接失败

检查：
- Redis 是否启动：`redis-cli ping`
- 端口是否正确
- 密码是否配置

### 3. JWT Token 失效

检查：
- Token 是否过期
- Secret 是否配置正确
- Header 格式：`Bearer {token}`

### 4. AI 接口调用失败

检查：
- API Key 是否配置
- 网络是否可访问 AI 服务
- 账户余额是否充足

## 📈 性能优化

- [ ] 使用 Redis 缓存热点数据
- [ ] MyBatis 二级缓存
- [ ] 数据库连接池优化
- [ ] 异步处理长时间任务
- [ ] 接口限流

## 🚢 部署

### Docker 部署

```bash
# 构建镜像
docker build -t ai-teacher-server .

# 运行容器
docker run -d -p 8080:8080 --name ai-teacher-server ai-teacher-server
```

### 传统部署

```bash
# 打包
mvn clean package -DskipTests

# 上传到服务器
scp target/ai-teacher-server-1.0.0.jar user@server:/app/

# 启动
java -jar ai-teacher-server-1.0.0.jar --spring.profiles.active=prod
```

## 📝 开发规范

### 代码规范

- 使用 Lombok 简化代码
- 所有接口返回统一响应格式
- 异常统一处理
- 日志规范记录

### Git 提交规范

```
feat: 新功能
fix: 修复bug
docs: 文档更新
refactor: 重构
test: 测试相关
chore: 构建/工具相关
```

## 📞 联系方式

如有问题，请联系开发团队。

---

**最后更新**: 2025-11-21


-- AI教师系统数据库脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS `ai_teacher` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `ai_teacher`;

-- ====================
-- 用户表
-- ====================
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `openid` VARCHAR(64) UNIQUE NOT NULL COMMENT '微信openid',
    `unionid` VARCHAR(64) COMMENT '微信unionid',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `avatar` VARCHAR(255) COMMENT '头像URL',
    `gender` TINYINT DEFAULT 0 COMMENT '性别：0未知 1男 2女',
    `phone` VARCHAR(11) COMMENT '手机号',
    `province_code` VARCHAR(10) COMMENT '省份编码',
    `grade` VARCHAR(10) COMMENT '年级',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0禁用 1正常',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记：0未删除 1已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_openid (`openid`),
    INDEX idx_unionid (`unionid`),
    INDEX idx_phone (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ====================
-- 对话表
-- ====================
CREATE TABLE IF NOT EXISTS `conversation` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '对话ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `title` VARCHAR(100) COMMENT '对话标题',
    `province_code` VARCHAR(10) COMMENT '省份编码',
    `grade` VARCHAR(10) COMMENT '年级',
    `subject` VARCHAR(20) COMMENT '学科',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0删除 1正常',
    `is_favorite` TINYINT DEFAULT 0 COMMENT '是否收藏：0否 1是',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记：0未删除 1已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (`user_id`),
    INDEX idx_create_time (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='对话表';

-- ====================
-- 消息表
-- ====================
CREATE TABLE IF NOT EXISTS `message` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID',
    `conversation_id` BIGINT NOT NULL COMMENT '对话ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role` VARCHAR(20) NOT NULL COMMENT '角色：user/assistant',
    `content_type` VARCHAR(20) NOT NULL COMMENT '内容类型：text/image/file/audio',
    `content` TEXT COMMENT '消息内容',
    `files` JSON COMMENT '附件列表',
    `ai_model` VARCHAR(50) COMMENT '使用的AI模型',
    `tokens` INT COMMENT '消耗的token数',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记：0未删除 1已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_conversation_id (`conversation_id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_create_time (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

-- ====================
-- 教材表
-- ====================
CREATE TABLE IF NOT EXISTS `textbook` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '教材ID',
    `name` VARCHAR(100) NOT NULL COMMENT '教材名称',
    `province_code` VARCHAR(10) NOT NULL COMMENT '省份编码',
    `grade` VARCHAR(10) NOT NULL COMMENT '年级',
    `subject` VARCHAR(20) NOT NULL COMMENT '学科',
    `version` VARCHAR(50) NOT NULL COMMENT '教材版本：人教版、苏教版等',
    `semester` TINYINT COMMENT '学期：1上 2下',
    `cover_url` VARCHAR(255) COMMENT '封面图',
    `file_url` VARCHAR(255) COMMENT '教材文件URL',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0禁用 1正常',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记：0未删除 1已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_province_grade_subject (`province_code`, `grade`, `subject`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教材表';

-- ====================
-- 教材内容表
-- ====================
CREATE TABLE IF NOT EXISTS `textbook_content` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '内容ID',
    `textbook_id` BIGINT NOT NULL COMMENT '教材ID',
    `chapter` VARCHAR(100) COMMENT '章节',
    `section` VARCHAR(100) COMMENT '小节',
    `content` TEXT COMMENT '内容',
    `page_number` INT COMMENT '页码',
    `knowledge_points` JSON COMMENT '知识点列表',
    `vector_id` VARCHAR(100) COMMENT '向量ID（向量数据库中的ID）',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记：0未删除 1已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_textbook_id (`textbook_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教材内容表';

-- ====================
-- 文件表
-- ====================
CREATE TABLE IF NOT EXISTS `file` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '文件ID',
    `user_id` BIGINT NOT NULL COMMENT '上传用户ID',
    `file_name` VARCHAR(255) NOT NULL COMMENT '文件名',
    `file_type` VARCHAR(50) NOT NULL COMMENT '文件类型',
    `file_size` BIGINT NOT NULL COMMENT '文件大小（字节）',
    `file_url` VARCHAR(500) NOT NULL COMMENT '文件URL',
    `thumbnail_url` VARCHAR(500) COMMENT '缩略图URL（图片）',
    `extracted_text` TEXT COMMENT '提取的文本内容',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0删除 1正常',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记：0未删除 1已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件表';

-- ====================
-- 扫码登录表
-- ====================
CREATE TABLE IF NOT EXISTS `qrcode_login` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '登录ID',
    `ticket` VARCHAR(100) UNIQUE NOT NULL COMMENT '登录票据',
    `qrcode_url` VARCHAR(500) NOT NULL COMMENT '二维码URL',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0待扫码 1已扫码 2已确认 3已取消 4已过期',
    `user_id` BIGINT COMMENT '用户ID（确认后）',
    `expire_time` DATETIME NOT NULL COMMENT '过期时间',
    `scan_time` DATETIME COMMENT '扫码时间',
    `confirm_time` DATETIME COMMENT '确认时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_ticket (`ticket`),
    INDEX idx_expire_time (`expire_time`),
    INDEX idx_user_id (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='扫码登录表';

-- ====================
-- 系统配置表
-- ====================
CREATE TABLE IF NOT EXISTS `system_config` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '配置ID',
    `config_key` VARCHAR(100) UNIQUE NOT NULL COMMENT '配置键',
    `config_value` TEXT COMMENT '配置值',
    `config_desc` VARCHAR(255) COMMENT '配置描述',
    `config_type` VARCHAR(20) DEFAULT 'string' COMMENT '配置类型：string/number/json',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记：0未删除 1已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_config_key (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- ====================
-- 插入初始数据
-- ====================

-- 插入测试用户
INSERT INTO `user` (`openid`, `nickname`, `avatar`, `gender`, `province_code`, `grade`, `status`)
VALUES ('test_openid_001', '测试用户', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png', 
        1, '11', 'middle_1', 1);

-- 插入系统配置
INSERT INTO `system_config` (`config_key`, `config_value`, `config_desc`, `config_type`)
VALUES 
    ('default_ai_model', 'qwen', '默认AI模型', 'string'),
    ('max_upload_size', '20971520', '最大上传文件大小（字节）', 'number'),
    ('qrcode_expire_time', '300', '二维码过期时间（秒）', 'number'),
    ('jwt_expire_time', '604800', 'JWT过期时间（秒）', 'number');


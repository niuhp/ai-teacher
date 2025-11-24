package com.aiteacher.server.common;

import lombok.Getter;

/**
 * 响应状态码枚举
 *
 * @author AI Teacher Team
 */
@Getter
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    FAIL(400, "操作失败"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权，请先登录"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "没有权限访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "请求的资源不存在"),

    /**
     * 请求方法不支持
     */
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),

    /**
     * 参数校验失败
     */
    VALIDATE_FAILED(422, "参数校验失败"),

    /**
     * 服务器错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(503, "服务暂时不可用"),

    // ========== 业务状态码 ==========

    /**
     * 用户相关
     */
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_DISABLED(1002, "用户已被禁用"),
    USER_ALREADY_EXISTS(1003, "用户已存在"),

    /**
     * 登录相关
     */
    LOGIN_FAILED(2001, "登录失败"),
    TOKEN_EXPIRED(2002, "登录已过期，请重新登录"),
    TOKEN_INVALID(2003, "无效的Token"),
    QRCODE_EXPIRED(2004, "二维码已过期"),
    QRCODE_CANCELLED(2005, "登录已取消"),

    /**
     * 文件相关
     */
    FILE_UPLOAD_FAILED(3001, "文件上传失败"),
    FILE_TOO_LARGE(3002, "文件大小超过限制"),
    FILE_TYPE_NOT_ALLOWED(3003, "不支持的文件类型"),

    /**
     * AI相关
     */
    AI_REQUEST_FAILED(4001, "AI请求失败"),
    AI_RESPONSE_ERROR(4002, "AI响应异常"),
    AI_MODEL_NOT_FOUND(4003, "AI模型不存在"),

    /**
     * 对话相关
     */
    CONVERSATION_NOT_FOUND(5001, "对话不存在"),
    MESSAGE_SEND_FAILED(5002, "消息发送失败"),

    /**
     * 教材相关
     */
    TEXTBOOK_NOT_FOUND(6001, "教材不存在");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}


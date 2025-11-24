package com.aiteacher.server.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息实体
 *
 * @author AI Teacher Team
 */
@Data
@TableName("message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 对话ID
     */
    private Long conversationId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色：user/assistant
     */
    private String role;

    /**
     * 内容类型：text/image/file/audio
     */
    private String contentType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 附件列表（JSON）
     */
    private String files;

    /**
     * 使用的AI模型
     */
    private String aiModel;

    /**
     * 消耗的token数
     */
    private Integer tokens;

    /**
     * 删除标记：0未删除 1已删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}


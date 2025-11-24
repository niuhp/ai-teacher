package com.aiteacher.server.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 对话实体
 *
 * @author AI Teacher Team
 */
@Data
@TableName("conversation")
public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对话ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 对话标题
     */
    private String title;

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 年级
     */
    private String grade;

    /**
     * 学科
     */
    private String subject;

    /**
     * 状态：0删除 1正常
     */
    private Integer status;

    /**
     * 是否收藏：0否 1是
     */
    private Integer isFavorite;

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

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}


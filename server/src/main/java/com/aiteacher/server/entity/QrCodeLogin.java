package com.aiteacher.server.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 扫码登录实体
 *
 * @author AI Teacher Team
 */
@Data
@TableName("qrcode_login")
public class QrCodeLogin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 登录票据
     */
    private String ticket;

    /**
     * 二维码URL
     */
    private String qrcodeUrl;

    /**
     * 状态：0待扫码 1已扫码 2已确认 3已取消 4已过期
     */
    private Integer status;

    /**
     * 用户ID（确认后）
     */
    private Long userId;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 扫码时间
     */
    private LocalDateTime scanTime;

    /**
     * 确认时间
     */
    private LocalDateTime confirmTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}


package com.aiteacher.server.dto;

import lombok.Data;

/**
 * 二维码登录DTO
 *
 * @author AI Teacher Team
 */
@Data
public class QrCodeLoginDTO {
    
    /**
     * 登录票据
     */
    private String ticket;
    
    /**
     * 二维码URL
     */
    private String qrcodeUrl;
    
    /**
     * 过期时间
     */
    private String expireTime;
}


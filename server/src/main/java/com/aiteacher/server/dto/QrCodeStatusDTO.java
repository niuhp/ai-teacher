package com.aiteacher.server.dto;

import lombok.Data;

/**
 * 二维码状态DTO
 *
 * @author AI Teacher Team
 */
@Data
public class QrCodeStatusDTO {
    
    /**
     * 状态：0待扫码 1已扫码 2已确认 3已取消 4已过期
     */
    private Integer status;
    
    /**
     * Token（已确认时返回）
     */
    private String token;
    
    /**
     * 用户信息（已确认时返回）
     */
    private UserInfoVO userInfo;
}


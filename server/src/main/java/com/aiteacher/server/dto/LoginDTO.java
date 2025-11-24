package com.aiteacher.server.dto;

import lombok.Data;

/**
 * 登录响应DTO
 *
 * @author AI Teacher Team
 */
@Data
public class LoginDTO {
    
    /**
     * JWT Token
     */
    private String token;
    
    /**
     * 用户信息
     */
    private UserInfoVO userInfo;
}


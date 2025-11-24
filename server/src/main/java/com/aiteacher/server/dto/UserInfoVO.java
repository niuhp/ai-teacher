package com.aiteacher.server.dto;

import lombok.Data;

/**
 * 用户信息VO
 *
 * @author AI Teacher Team
 */
@Data
public class UserInfoVO {
    
    private Long id;
    private String openid;
    private String nickname;
    private String avatar;
    private Integer gender;
    private String phone;
    private String provinceCode;
    private String provinceName;
    private String grade;
    private String gradeName;
    private Integer status;
    private String createTime;
}


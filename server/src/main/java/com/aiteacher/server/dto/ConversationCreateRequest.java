package com.aiteacher.server.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 创建对话请求
 *
 * @author AI Teacher Team
 */
@Data
public class ConversationCreateRequest {
    
    @NotBlank(message = "省份编码不能为空")
    private String provinceCode;
    
    @NotBlank(message = "年级不能为空")
    private String grade;
    
    @NotBlank(message = "学科不能为空")
    private String subject;
}


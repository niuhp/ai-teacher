package com.aiteacher.server.dto;

import lombok.Data;

/**
 * 对话VO
 *
 * @author AI Teacher Team
 */
@Data
public class ConversationVO {
    
    private Long id;
    private Long userId;
    private String title;
    private String provinceCode;
    private String grade;
    private String subject;
    private Integer status;
    private Integer isFavorite;
    private String lastMessage;
    private String createTime;
    private String updateTime;
}


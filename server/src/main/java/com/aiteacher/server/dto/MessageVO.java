package com.aiteacher.server.dto;

import lombok.Data;

import java.util.List;

/**
 * 消息VO
 *
 * @author AI Teacher Team
 */
@Data
public class MessageVO {
    
    private Long id;
    private Long conversationId;
    private Long userId;
    private String role;
    private String contentType;
    private String content;
    private List<MessageSendRequest.FileItemDTO> files;
    private String aiModel;
    private Integer tokens;
    private String createTime;
}


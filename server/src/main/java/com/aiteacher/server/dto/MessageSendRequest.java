package com.aiteacher.server.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * 发送消息请求
 *
 * @author AI Teacher Team
 */
@Data
public class MessageSendRequest {
    
    @NotBlank(message = "内容类型不能为空")
    private String contentType;
    
    @NotBlank(message = "内容不能为空")
    private String content;
    
    private List<FileItemDTO> files;
    
    private String aiModel;
    
    @Data
    public static class FileItemDTO {
        private Long fileId;
        private String fileUrl;
        private String fileName;
        private String fileType;
        private Long fileSize;
    }
}


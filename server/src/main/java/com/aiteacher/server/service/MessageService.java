package com.aiteacher.server.service;

import com.aiteacher.server.dto.MessageSendRequest;
import com.aiteacher.server.dto.MessageVO;

/**
 * 消息服务接口
 *
 * @author AI Teacher Team
 */
public interface MessageService {

    /**
     * 发送消息
     */
    MessageVO sendMessage(Long conversationId, Long userId, MessageSendRequest request);

    /**
     * 保存用户消息
     */
    void saveUserMessage(Long conversationId, Long userId, MessageSendRequest request);

    /**
     * 保存AI回复
     */
    MessageVO saveAssistantMessage(Long conversationId, Long userId, String content, String aiModel, Integer tokens);
}


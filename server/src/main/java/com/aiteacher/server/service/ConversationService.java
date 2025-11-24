package com.aiteacher.server.service;

import com.aiteacher.server.dto.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 对话服务接口
 *
 * @author AI Teacher Team
 */
public interface ConversationService {

    /**
     * 创建对话
     */
    Long createConversation(Long userId, ConversationCreateRequest request);

    /**
     * 获取对话列表
     */
    Page<ConversationVO> getConversationList(Long userId, Integer page, Integer size, String subject);

    /**
     * 获取对话详情
     */
    ConversationDetailVO getConversationDetail(Long conversationId, Long userId);

    /**
     * 删除对话
     */
    void deleteConversation(Long conversationId, Long userId);

    /**
     * 收藏/取消收藏对话
     */
    void toggleFavorite(Long conversationId, Long userId, Boolean isFavorite);

    /**
     * 更新对话标题
     */
    void updateTitle(Long conversationId, Long userId, String title);

    /**
     * 对话详情VO
     */
    class ConversationDetailVO {
        public Long id;
        public String title;
        public List<MessageVO> messages;
    }
}

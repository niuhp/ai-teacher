package com.aiteacher.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.aiteacher.server.common.ResultCode;
import com.aiteacher.server.dto.ConversationCreateRequest;
import com.aiteacher.server.dto.ConversationVO;
import com.aiteacher.server.dto.MessageVO;
import com.aiteacher.server.entity.Conversation;
import com.aiteacher.server.entity.Message;
import com.aiteacher.server.exception.BusinessException;
import com.aiteacher.server.mapper.ConversationMapper;
import com.aiteacher.server.mapper.MessageMapper;
import com.aiteacher.server.service.ConversationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 对话服务实现
 *
 * @author AI Teacher Team
 */
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationMapper conversationMapper;
    private final MessageMapper messageMapper;

    @Override
    public Long createConversation(Long userId, ConversationCreateRequest request) {
        Conversation conversation = new Conversation();
        conversation.setUserId(userId);
        conversation.setProvinceCode(request.getProvinceCode());
        conversation.setGrade(request.getGrade());
        conversation.setSubject(request.getSubject());
        conversation.setTitle("新对话"); // 默认标题
        conversation.setStatus(1);
        conversation.setIsFavorite(0);
        
        conversationMapper.insert(conversation);
        return conversation.getId();
    }

    @Override
    public Page<ConversationVO> getConversationList(Long userId, Integer page, Integer size, String subject) {
        Page<Conversation> pageParam = new Page<>(page, size);
        
        LambdaQueryWrapper<Conversation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Conversation::getUserId, userId);
        wrapper.eq(Conversation::getStatus, 1);
        
        if (StrUtil.isNotBlank(subject)) {
            wrapper.eq(Conversation::getSubject, subject);
        }
        
        wrapper.orderByDesc(Conversation::getUpdateTime);
        
        Page<Conversation> conversationPage = conversationMapper.selectPage(pageParam, wrapper);
        
        // 转换为VO
        Page<ConversationVO> voPage = new Page<>();
        voPage.setCurrent(conversationPage.getCurrent());
        voPage.setSize(conversationPage.getSize());
        voPage.setTotal(conversationPage.getTotal());
        
        List<ConversationVO> voList = conversationPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        
        return voPage;
    }

    @Override
    public ConversationDetailVO getConversationDetail(Long conversationId, Long userId) {
        Conversation conversation = getConversationById(conversationId, userId);
        
        // 查询消息列表
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getConversationId, conversationId);
        wrapper.orderByAsc(Message::getCreateTime);
        
        List<Message> messages = messageMapper.selectList(wrapper);
        
        ConversationDetailVO vo = new ConversationDetailVO();
        vo.id = conversation.getId();
        vo.title = conversation.getTitle();
        vo.messages = messages.stream()
                .map(this::convertMessageToVO)
                .collect(Collectors.toList());
        
        return vo;
    }

    @Override
    public void deleteConversation(Long conversationId, Long userId) {
        Conversation conversation = getConversationById(conversationId, userId);
        conversation.setStatus(0);
        conversationMapper.updateById(conversation);
    }

    @Override
    public void toggleFavorite(Long conversationId, Long userId, Boolean isFavorite) {
        Conversation conversation = getConversationById(conversationId, userId);
        conversation.setIsFavorite(isFavorite ? 1 : 0);
        conversationMapper.updateById(conversation);
    }

    @Override
    public void updateTitle(Long conversationId, Long userId, String title) {
        Conversation conversation = getConversationById(conversationId, userId);
        conversation.setTitle(title);
        conversationMapper.updateById(conversation);
    }

    /**
     * 获取对话并验证权限
     */
    private Conversation getConversationById(Long conversationId, Long userId) {
        Conversation conversation = conversationMapper.selectById(conversationId);
        if (conversation == null || conversation.getStatus() == 0) {
            throw new BusinessException(ResultCode.CONVERSATION_NOT_FOUND);
        }
        if (!conversation.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        return conversation;
    }

    /**
     * 转换为VO
     */
    private ConversationVO convertToVO(Conversation conversation) {
        ConversationVO vo = new ConversationVO();
        BeanUtil.copyProperties(conversation, vo);
        vo.setCreateTime(conversation.getCreateTime().toString());
        vo.setUpdateTime(conversation.getUpdateTime().toString());
        
        // 获取最后一条消息
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getConversationId, conversation.getId());
        wrapper.orderByDesc(Message::getCreateTime);
        wrapper.last("LIMIT 1");
        Message lastMessage = messageMapper.selectOne(wrapper);
        
        if (lastMessage != null) {
            vo.setLastMessage(StrUtil.sub(lastMessage.getContent(), 0, 50));
        }
        
        return vo;
    }

    /**
     * 消息转VO
     */
    private MessageVO convertMessageToVO(Message message) {
        MessageVO vo = new MessageVO();
        BeanUtil.copyProperties(message, vo);
        vo.setCreateTime(message.getCreateTime().toString());
        // TODO: 解析files字段（JSON）
        return vo;
    }
}


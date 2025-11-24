package com.aiteacher.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.aiteacher.server.dto.MessageSendRequest;
import com.aiteacher.server.dto.MessageVO;
import com.aiteacher.server.entity.Message;
import com.aiteacher.server.mapper.MessageMapper;
import com.aiteacher.server.service.MessageService;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 消息服务实现
 *
 * @author AI Teacher Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;

    @Override
    public MessageVO sendMessage(Long conversationId, Long userId, MessageSendRequest request) {
        // 保存用户消息
        saveUserMessage(conversationId, userId, request);
        
        // 获取AI回复（暂时返回固定内容）
        String aiResponse = generateMockAIResponse(request.getContent());
        String aiModel = StrUtil.isNotBlank(request.getAiModel()) ? request.getAiModel() : "qwen";
        
        // 保存AI回复
        return saveAssistantMessage(conversationId, userId, aiResponse, aiModel, 100);
    }

    @Override
    public void saveUserMessage(Long conversationId, Long userId, MessageSendRequest request) {
        Message message = new Message();
        message.setConversationId(conversationId);
        message.setUserId(userId);
        message.setRole("user");
        message.setContentType(request.getContentType());
        message.setContent(request.getContent());
        
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            message.setFiles(JSON.toJSONString(request.getFiles()));
        }
        
        messageMapper.insert(message);
    }

    @Override
    public MessageVO saveAssistantMessage(Long conversationId, Long userId, String content, String aiModel, Integer tokens) {
        Message message = new Message();
        message.setConversationId(conversationId);
        message.setUserId(userId);
        message.setRole("assistant");
        message.setContentType("text");
        message.setContent(content);
        message.setAiModel(aiModel);
        message.setTokens(tokens);
        
        messageMapper.insert(message);
        
        // 转换为VO
        MessageVO vo = new MessageVO();
        BeanUtil.copyProperties(message, vo);
        vo.setCreateTime(message.getCreateTime().toString());
        
        return vo;
    }

    /**
     * 生成Mock AI回复
     */
    private String generateMockAIResponse(String userQuestion) {
        return String.format("""
                # AI教师回复
                
                您的问题是："%s"
                
                ## 这是一个Mock回复
                
                由于AI模型尚未接入，这是一个测试回复。真实的AI回答会更加详细和准确。
                
                ### 示例功能展示
                
                1. **Markdown格式支持**
                   - 列表项1
                   - 列表项2
                
                2. **数学公式支持**
                   - 行内公式：$x^2 + y^2 = z^2$
                   - 块级公式：
                
                $$
                f(x) = \\int_{-\\infty}^{\\infty} e^{-x^2} dx
                $$
                
                3. **代码高亮支持**
                
                ```python
                def hello_world():
                    print("Hello, AI Teacher!")
                ```
                
                ### 下一步
                
                接入真实的AI模型后，您将获得：
                - 详细的问题分析
                - 分步骤的解题过程
                - 相关知识点讲解
                - 习题推荐
                
                感谢使用AI教师系统！🎓
                """, userQuestion);
    }
}


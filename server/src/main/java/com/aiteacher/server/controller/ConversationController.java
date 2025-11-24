package com.aiteacher.server.controller;

import com.aiteacher.server.common.PageResult;
import com.aiteacher.server.common.Result;
import com.aiteacher.server.dto.*;
import com.aiteacher.server.service.ConversationService;
import com.aiteacher.server.service.MessageService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 对话控制器
 *
 * @author AI Teacher Team
 */
@RestController
@RequestMapping("/conversation")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;
    private final MessageService messageService;

    /**
     * 创建对话
     */
    @PostMapping("/create")
    public Result<Map<String, Long>> createConversation(@Valid @RequestBody ConversationCreateRequest request) {
        // TODO: 从JWT中获取当前用户ID
        Long userId = 1L;
        
        Long conversationId = conversationService.createConversation(userId, request);
        
        Map<String, Long> result = new HashMap<>();
        result.put("conversationId", conversationId);
        
        return Result.success(result);
    }

    /**
     * 获取对话列表
     */
    @GetMapping("/list")
    public Result<PageResult<ConversationVO>> getConversationList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String subject) {
        
        // TODO: 从JWT中获取当前用户ID
        Long userId = 1L;
        
        Page<ConversationVO> pageResult = conversationService.getConversationList(userId, page, size, subject);
        
        PageResult<ConversationVO> result = PageResult.success(
                pageResult.getRecords(),
                pageResult.getTotal(),
                (int) pageResult.getCurrent(),
                (int) pageResult.getSize()
        );
        
        return Result.success(result);
    }

    /**
     * 获取对话详情
     */
    @GetMapping("/{id}")
    public Result<ConversationService.ConversationDetailVO> getConversationDetail(@PathVariable Long id) {
        // TODO: 从JWT中获取当前用户ID
        Long userId = 1L;
        
        ConversationService.ConversationDetailVO detail = conversationService.getConversationDetail(id, userId);
        return Result.success(detail);
    }

    /**
     * 发送消息
     */
    @PostMapping("/{id}/message")
    public Result<MessageVO> sendMessage(
            @PathVariable Long id,
            @Valid @RequestBody MessageSendRequest request) {
        
        // TODO: 从JWT中获取当前用户ID
        Long userId = 1L;
        
        MessageVO message = messageService.sendMessage(id, userId, request);
        return Result.success(message);
    }

    /**
     * 删除对话
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteConversation(@PathVariable Long id) {
        // TODO: 从JWT中获取当前用户ID
        Long userId = 1L;
        
        conversationService.deleteConversation(id, userId);
        return Result.success("删除成功");
    }

    /**
     * 收藏/取消收藏对话
     */
    @PutMapping("/{id}/favorite")
    public Result<Void> toggleFavorite(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> params) {
        
        // TODO: 从JWT中获取当前用户ID
        Long userId = 1L;
        
        Boolean isFavorite = params.get("isFavorite");
        conversationService.toggleFavorite(id, userId, isFavorite);
        return Result.success(isFavorite ? "已收藏" : "已取消收藏");
    }

    /**
     * 更新对话标题
     */
    @PutMapping("/{id}/title")
    public Result<Void> updateTitle(
            @PathVariable Long id,
            @RequestBody Map<String, String> params) {
        
        // TODO: 从JWT中获取当前用户ID
        Long userId = 1L;
        
        String title = params.get("title");
        conversationService.updateTitle(id, userId, title);
        return Result.success("更新成功");
    }
}


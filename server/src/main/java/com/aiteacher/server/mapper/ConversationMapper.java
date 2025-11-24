package com.aiteacher.server.mapper;

import com.aiteacher.server.entity.Conversation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 对话Mapper
 *
 * @author AI Teacher Team
 */
@Mapper
public interface ConversationMapper extends BaseMapper<Conversation> {
}


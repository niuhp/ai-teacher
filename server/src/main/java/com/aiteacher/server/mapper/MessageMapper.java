package com.aiteacher.server.mapper;

import com.aiteacher.server.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息Mapper
 *
 * @author AI Teacher Team
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}


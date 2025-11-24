package com.aiteacher.server.mapper;

import com.aiteacher.server.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 *
 * @author AI Teacher Team
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}


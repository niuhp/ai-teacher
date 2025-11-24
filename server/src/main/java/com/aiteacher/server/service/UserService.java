package com.aiteacher.server.service;

import com.aiteacher.server.dto.UserInfoVO;
import com.aiteacher.server.entity.User;

/**
 * 用户服务接口
 *
 * @author AI Teacher Team
 */
public interface UserService {

    /**
     * 根据ID获取用户
     */
    User getUserById(Long userId);

    /**
     * 根据openid获取用户
     */
    User getUserByOpenid(String openid);

    /**
     * 创建或更新用户
     */
    User createOrUpdateUser(User user);

    /**
     * 获取用户信息VO
     */
    UserInfoVO getUserInfo(Long userId);

    /**
     * 更新用户信息
     */
    void updateUserInfo(Long userId, User user);
}


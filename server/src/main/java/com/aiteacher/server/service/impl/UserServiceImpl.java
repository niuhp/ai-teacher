package com.aiteacher.server.service.impl;

import com.aiteacher.server.common.ResultCode;
import com.aiteacher.server.dto.UserInfoVO;
import com.aiteacher.server.entity.User;
import com.aiteacher.server.exception.BusinessException;
import com.aiteacher.server.mapper.UserMapper;
import com.aiteacher.server.service.UserService;
import com.aiteacher.server.util.Constants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现
 *
 * @author AI Teacher Team
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public User getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }
        return user;
    }

    @Override
    public User getUserByOpenid(String openid) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User createOrUpdateUser(User user) {
        User existUser = getUserByOpenid(user.getOpenid());
        if (existUser != null) {
            // 更新用户信息
            existUser.setNickname(user.getNickname());
            existUser.setAvatar(user.getAvatar());
            existUser.setGender(user.getGender());
            existUser.setUnionid(user.getUnionid());
            userMapper.updateById(existUser);
            return existUser;
        } else {
            // 创建新用户
            user.setStatus(1);
            userMapper.insert(user);
            return user;
        }
    }

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        User user = getUserById(userId);
        
        UserInfoVO vo = new UserInfoVO();
        BeanUtils.copyProperties(user, vo);
        
        // 补充省份和年级名称
        vo.setProvinceName(Constants.getProvinceName(user.getProvinceCode()));
        vo.setGradeName(Constants.getGradeName(user.getGrade()));
        vo.setCreateTime(user.getCreateTime().toString());
        
        return vo;
    }

    @Override
    public void updateUserInfo(Long userId, User user) {
        User existUser = getUserById(userId);
        
        if (user.getNickname() != null) {
            existUser.setNickname(user.getNickname());
        }
        if (user.getPhone() != null) {
            existUser.setPhone(user.getPhone());
        }
        if (user.getProvinceCode() != null) {
            existUser.setProvinceCode(user.getProvinceCode());
        }
        if (user.getGrade() != null) {
            existUser.setGrade(user.getGrade());
        }
        
        userMapper.updateById(existUser);
    }
}


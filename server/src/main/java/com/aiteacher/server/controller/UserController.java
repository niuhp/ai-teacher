package com.aiteacher.server.controller;

import com.aiteacher.server.common.Result;
import com.aiteacher.server.dto.UserInfoVO;
import com.aiteacher.server.entity.User;
import com.aiteacher.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author AI Teacher Team
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo() {
        // TODO: 从JWT中获取当前用户ID
        // 暂时使用测试用户ID
        Long userId = 1L;
        UserInfoVO userInfo = userService.getUserInfo(userId);
        return Result.success(userInfo);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/info")
    public Result<Void> updateUserInfo(@RequestBody User user) {
        // TODO: 从JWT中获取当前用户ID
        Long userId = 1L;
        userService.updateUserInfo(userId, user);
        return Result.success("更新成功");
    }
}

package com.aiteacher.server.controller;

import com.aiteacher.server.common.Result;
import com.aiteacher.server.dto.QrCodeLoginDTO;
import com.aiteacher.server.dto.QrCodeStatusDTO;
import com.aiteacher.server.service.QrCodeLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author AI Teacher Team
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final QrCodeLoginService qrCodeLoginService;

    /**
     * 生成登录二维码
     */
    @PostMapping("/qrcode/generate")
    public Result<QrCodeLoginDTO> generateQrCode() {
        QrCodeLoginDTO dto = qrCodeLoginService.generateQrCode();
        return Result.success(dto);
    }

    /**
     * 查询登录状态
     */
    @GetMapping("/qrcode/status")
    public Result<QrCodeStatusDTO> getQrCodeStatus(@RequestParam String ticket) {
        QrCodeStatusDTO dto = qrCodeLoginService.getQrCodeStatus(ticket);
        return Result.success(dto);
    }

    /**
     * 确认登录（小程序端调用）
     */
    @PostMapping("/qrcode/confirm")
    public Result<Void> confirmQrCodeLogin(
            @RequestParam String ticket,
            @RequestParam(defaultValue = "true") Boolean confirm) {
        // TODO: 从token中获取当前用户ID
        // 这里暂时使用测试用户ID
        Long userId = 1L;
        qrCodeLoginService.confirmLogin(ticket, userId, confirm);
        return Result.success();
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        // 实际场景可以将token加入黑名单
        return Result.success("退出成功");
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refresh-token")
    public Result<String> refreshToken() {
        // TODO: 实现token刷新逻辑
        return Result.success("刷新成功");
    }
}

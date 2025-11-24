package com.aiteacher.server.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.aiteacher.server.common.ResultCode;
import com.aiteacher.server.dto.LoginDTO;
import com.aiteacher.server.dto.QrCodeLoginDTO;
import com.aiteacher.server.dto.QrCodeStatusDTO;
import com.aiteacher.server.entity.QrCodeLogin;
import com.aiteacher.server.entity.User;
import com.aiteacher.server.exception.BusinessException;
import com.aiteacher.server.mapper.QrCodeLoginMapper;
import com.aiteacher.server.service.QrCodeLoginService;
import com.aiteacher.server.service.UserService;
import com.aiteacher.server.util.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 扫码登录服务实现
 *
 * @author AI Teacher Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QrCodeLoginServiceImpl implements QrCodeLoginService {

    private final QrCodeLoginMapper qrCodeLoginMapper;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Value("${ai-teacher.jwt.expiration:300}")
    private Integer qrCodeExpireTime;

    @Override
    public QrCodeLoginDTO generateQrCode() {
        // 生成唯一ticket
        String ticket = UUID.randomUUID().toString(true);
        
        // 生成二维码URL（这里简化处理，实际应该生成真正的二维码图片）
        String qrcodeUrl = "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=" + ticket;
        
        // 过期时间（默认5分钟）
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(qrCodeExpireTime);
        
        // 保存到数据库
        QrCodeLogin qrCodeLogin = new QrCodeLogin();
        qrCodeLogin.setTicket(ticket);
        qrCodeLogin.setQrcodeUrl(qrcodeUrl);
        qrCodeLogin.setStatus(0); // 待扫码
        qrCodeLogin.setExpireTime(expireTime);
        qrCodeLoginMapper.insert(qrCodeLogin);
        
        QrCodeLoginDTO dto = new QrCodeLoginDTO();
        dto.setTicket(ticket);
        dto.setQrcodeUrl(qrcodeUrl);
        dto.setExpireTime(expireTime.toString());
        
        return dto;
    }

    @Override
    public QrCodeStatusDTO getQrCodeStatus(String ticket) {
        if (StrUtil.isBlank(ticket)) {
            throw new BusinessException("ticket不能为空");
        }
        
        QrCodeLogin qrCodeLogin = getQrCodeLoginByTicket(ticket);
        
        // 检查是否过期
        if (LocalDateTime.now().isAfter(qrCodeLogin.getExpireTime())) {
            qrCodeLogin.setStatus(4); // 已过期
            qrCodeLoginMapper.updateById(qrCodeLogin);
            throw new BusinessException(ResultCode.QRCODE_EXPIRED);
        }
        
        QrCodeStatusDTO dto = new QrCodeStatusDTO();
        dto.setStatus(qrCodeLogin.getStatus());
        
        // 如果已确认，返回token和用户信息
        if (qrCodeLogin.getStatus() == 2 && qrCodeLogin.getUserId() != null) {
            User user = userService.getUserById(qrCodeLogin.getUserId());
            String token = jwtUtil.generateToken(user.getId(), user.getNickname());
            
            dto.setToken(token);
            dto.setUserInfo(userService.getUserInfo(user.getId()));
        }
        
        return dto;
    }

    @Override
    public void scanQrCode(String ticket, Long userId) {
        QrCodeLogin qrCodeLogin = getQrCodeLoginByTicket(ticket);
        
        // 检查是否过期
        if (LocalDateTime.now().isAfter(qrCodeLogin.getExpireTime())) {
            throw new BusinessException(ResultCode.QRCODE_EXPIRED);
        }
        
        // 更新状态为已扫码
        qrCodeLogin.setStatus(1);
        qrCodeLogin.setUserId(userId);
        qrCodeLogin.setScanTime(LocalDateTime.now());
        qrCodeLoginMapper.updateById(qrCodeLogin);
    }

    @Override
    public LoginDTO confirmLogin(String ticket, Long userId, boolean confirm) {
        QrCodeLogin qrCodeLogin = getQrCodeLoginByTicket(ticket);
        
        // 检查是否过期
        if (LocalDateTime.now().isAfter(qrCodeLogin.getExpireTime())) {
            throw new BusinessException(ResultCode.QRCODE_EXPIRED);
        }
        
        if (!confirm) {
            // 取消登录
            qrCodeLogin.setStatus(3);
            qrCodeLoginMapper.updateById(qrCodeLogin);
            throw new BusinessException(ResultCode.QRCODE_CANCELLED);
        }
        
        // 确认登录
        qrCodeLogin.setStatus(2);
        qrCodeLogin.setUserId(userId);
        qrCodeLogin.setConfirmTime(LocalDateTime.now());
        qrCodeLoginMapper.updateById(qrCodeLogin);
        
        // 生成token
        User user = userService.getUserById(userId);
        String token = jwtUtil.generateToken(user.getId(), user.getNickname());
        
        LoginDTO dto = new LoginDTO();
        dto.setToken(token);
        dto.setUserInfo(userService.getUserInfo(user.getId()));
        
        return dto;
    }

    @Override
    public void cleanExpiredQrCodes() {
        LambdaQueryWrapper<QrCodeLogin> wrapper = new LambdaQueryWrapper<>();
        wrapper.lt(QrCodeLogin::getExpireTime, LocalDateTime.now());
        wrapper.ne(QrCodeLogin::getStatus, 4);
        
        QrCodeLogin update = new QrCodeLogin();
        update.setStatus(4);
        qrCodeLoginMapper.update(update, wrapper);
    }

    /**
     * 根据ticket获取二维码登录记录
     */
    private QrCodeLogin getQrCodeLoginByTicket(String ticket) {
        LambdaQueryWrapper<QrCodeLogin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QrCodeLogin::getTicket, ticket);
        QrCodeLogin qrCodeLogin = qrCodeLoginMapper.selectOne(wrapper);
        
        if (qrCodeLogin == null) {
            throw new BusinessException("二维码不存在");
        }
        
        return qrCodeLogin;
    }
}


package com.aiteacher.server.service;

import com.aiteacher.server.dto.LoginDTO;
import com.aiteacher.server.dto.QrCodeLoginDTO;
import com.aiteacher.server.dto.QrCodeStatusDTO;

/**
 * 扫码登录服务接口
 *
 * @author AI Teacher Team
 */
public interface QrCodeLoginService {

    /**
     * 生成登录二维码
     */
    QrCodeLoginDTO generateQrCode();

    /**
     * 查询登录状态
     */
    QrCodeStatusDTO getQrCodeStatus(String ticket);

    /**
     * 扫码（小程序端调用）
     */
    void scanQrCode(String ticket, Long userId);

    /**
     * 确认登录（小程序端调用）
     */
    LoginDTO confirmLogin(String ticket, Long userId, boolean confirm);

    /**
     * 清理过期的二维码
     */
    void cleanExpiredQrCodes();
}


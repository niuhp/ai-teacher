package com.aiteacher.server.mapper;

import com.aiteacher.server.entity.QrCodeLogin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 扫码登录Mapper
 *
 * @author AI Teacher Team
 */
@Mapper
public interface QrCodeLoginMapper extends BaseMapper<QrCodeLogin> {
}


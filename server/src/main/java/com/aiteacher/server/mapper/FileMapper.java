package com.aiteacher.server.mapper;

import com.aiteacher.server.entity.FileInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件Mapper
 *
 * @author AI Teacher Team
 */
@Mapper
public interface FileMapper extends BaseMapper<FileInfo> {
}


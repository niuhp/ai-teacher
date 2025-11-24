package com.aiteacher.server.controller;

import com.aiteacher.server.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 *
 * @author AI Teacher Team
 */
@RestController
public class HealthController {

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("timestamp", LocalDateTime.now().toString());
        data.put("application", "AI Teacher Server");
        data.put("version", "1.0.0");
        
        return Result.success(data);
    }

    /**
     * 根路径
     */
    @GetMapping("/")
    public Result<String> index() {
        return Result.success("AI教师系统后端服务运行中...");
    }
}


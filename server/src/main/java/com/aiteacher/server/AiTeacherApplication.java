package com.aiteacher.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AI教师应用启动类
 *
 * @author AI Teacher Team
 * @since 2025-11-21
 */
@SpringBootApplication
@MapperScan("com.aiteacher.server.mapper")
public class AiTeacherApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiTeacherApplication.class, args);
        System.out.println("""
                
                ====================================
                🎓 AI教师系统启动成功！
                📚 智能学习助手已就绪
                🌐 访问地址: http://localhost:8080/api
                ====================================
                """);
    }
}


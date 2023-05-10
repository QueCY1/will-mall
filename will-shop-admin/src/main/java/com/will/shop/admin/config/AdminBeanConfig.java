package com.will.shop.admin.config;

import cn.hutool.core.lang.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式唯一 ID 生成器 snowflake
 * @author will
 */
@Configuration
@RequiredArgsConstructor
public class AdminBeanConfig {

    private final AdminConfig adminConfig;

    @Bean
    public Snowflake snowflake() {
        return new Snowflake(adminConfig.getWorkerId(), adminConfig.getDatacenterId());
    }
}

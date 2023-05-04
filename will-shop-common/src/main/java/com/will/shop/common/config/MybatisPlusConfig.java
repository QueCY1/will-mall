package com.will.shop.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.will.shop.**.dao"})
public class MybatisPlusConfig {
}

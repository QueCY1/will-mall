package com.will.shop.common.config;

import com.will.shop.common.util.ImgUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置静态资源的访问路径映射，注册(registry)这个就可以通过该服务端口 + "/willMall/img/**" 访问到本地文件
 * @author will
 */
@Configuration
@RequiredArgsConstructor
public class ResourceConfigAdapter implements WebMvcConfigurer {

    private final ImgUploadUtil imgUploadUtil;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/willMall/img/**").addResourceLocations("file:" + imgUploadUtil.getUploadPath());
    }
}

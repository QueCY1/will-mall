package com.will.shop.security.admin.adapter;

import cn.hutool.core.collection.CollectionUtil;
import com.will.shop.security.common.adapter.DefaultAuthConfigAdapter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 继承默认的验证适配器，商户端添加不需要验证的url
 *
 * @author will
 * @date 2022/3/28 14:57
 */
@Component
public class ResourceServerAdapter extends DefaultAuthConfigAdapter {
    public static final List<String> EXCLUDE_PATH = Arrays.asList(
            "/webjars/**",
            "/swagger/**",
            "/v3/api-docs/**",
            "/doc.html",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/captcha/**",
            "/adminLogin",
            "/mall4j/img/**");

    @Override
    public List<String> excludePathPatterns() {
        return EXCLUDE_PATH;
    }
}
package com.will.shop.common.config;

import cn.hutool.crypto.symmetric.AES;
import com.will.shop.common.bean.AliDaYu;
import com.will.shop.common.bean.ImgUpload;
import com.will.shop.common.bean.Qiniu;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ShopBeanConfig {

    private final ShopBasicConfig shopBasicConfig;

    @Bean
    public Qiniu qiniu() {
        return shopBasicConfig.getQiniu();
    }

    @Bean
    public AliDaYu aLiDaYu () {
        return shopBasicConfig.getALiDaYu();
    }

    @Bean
    public ImgUpload imgUpload() {
        return shopBasicConfig.getImgUpload();
    }

}

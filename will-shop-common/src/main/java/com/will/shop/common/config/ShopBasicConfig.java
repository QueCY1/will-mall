package com.will.shop.common.config;

import com.will.shop.common.bean.AliDaYu;
import com.will.shop.common.bean.ImgUpload;
import com.will.shop.common.bean.Qiniu;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * 商城配置文件
 *
 * @author will
 */
@Data
@Component
@PropertySource("classpath:shop.properties")
@ConfigurationProperties(prefix = "shop")
public class ShopBasicConfig {

    /**
     * 七牛云的配置信息
     */
    private Qiniu qiniu;

    /**
     * 阿里大于短信平台
     */
    private AliDaYu aLiDaYu;

    /**
     * 用于加解密token的密钥
     */
    private String tokenAesKey;

    /**
     * 本地文件上传配置
     */
    private ImgUpload imgUpload;

}

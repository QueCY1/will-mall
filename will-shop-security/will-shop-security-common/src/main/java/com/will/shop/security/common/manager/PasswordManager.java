package com.will.shop.security.common.manager;

import cn.hutool.crypto.symmetric.AES;
import com.will.shop.common.exception.WillShopBindException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author 菠萝凤梨
 * @date 2022/1/19 16:02
 */
@Component
public class PasswordManager {
    private static final Logger logger = LoggerFactory.getLogger(PasswordManager.class);

    /**
     * 用于aes签名的key，16位
     */
    @Value("${auth.password.signKey:-will--password-}")
    public String passwordSignKey;

    public String decryptPassword(String data) {
        AES aes = new AES(passwordSignKey.getBytes(StandardCharsets.UTF_8));
        String decryptStr;
        String decryptPassword;
        try {
            decryptStr = aes.decryptStr(data);
            decryptPassword = decryptStr.substring(13);
        } catch (Exception e) {
            logger.error("Exception:", e);
            throw new WillShopBindException("AES解密错误", e);
        }
        return decryptPassword;
    }
}

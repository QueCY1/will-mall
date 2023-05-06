package com.will.shop.common.handler;

import cn.hutool.core.util.CharsetUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.response.ServerResponseEntity;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @author 菠萝凤梨
 * @date 2022/3/28 14:15
 */
@Component
public class HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(HttpHandler.class);

    @Autowired
    private ObjectMapper objectMapper;

    public <T> void printServerResponseToWeb(ServerResponseEntity<T> serverResponseEntity) {
        if (serverResponseEntity == null) {
            logger.info("print obj is null");
            return;
        }

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (requestAttributes == null) {
            logger.error("requestAttributes is null, can not print to web");
            return;
        }
        HttpServletResponse response = requestAttributes.getResponse();
        if (response == null) {
            logger.error("httpServletResponse is null, can not print to web");
            return;
        }
        logger.error("response error: " + serverResponseEntity.getMsg());
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            printWriter.write(objectMapper.writeValueAsString(serverResponseEntity));
        }
        catch (IOException e) {
            throw new WillShopBindException("io 异常", e);
        }
    }

    public <T> void printServerResponseToWeb(WillShopBindException WillShopBindException) {
        if (WillShopBindException == null) {
            logger.info("print obj is null");
            return;
        }

        if (Objects.nonNull(WillShopBindException.getServerResponseEntity())) {
            printServerResponseToWeb(WillShopBindException.getServerResponseEntity());
            return;
        }

        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(WillShopBindException.getCode());
        serverResponseEntity.setMsg(WillShopBindException.getMessage());
        printServerResponseToWeb(serverResponseEntity);
    }
}

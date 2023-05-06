package com.will.shop.common.filter;

import com.will.shop.common.xss.XssWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 一些简单的安全过滤
 * xss
 * @author will
 */
@Component
public class XssFilter implements Filter {

    Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        logger.info("uri:{}", request.getRequestURI());
        // xss 过滤
        filterChain.doFilter(new XssWrapper(request), response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}

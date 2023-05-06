package com.will.shop.security.common.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.handler.HttpHandler;
import com.will.shop.common.response.ResponseEnum;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.security.common.adapter.AuthConfigAdapter;
import com.will.shop.security.common.bo.UserInfoInTokenBO;
import com.will.shop.security.common.manager.TokenStore;
import com.will.shop.security.common.util.AuthUserContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    private final AuthConfigAdapter authConfigAdapter;

    private final HttpHandler httpHandler;

    private final TokenStore tokenStore;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestUri = request.getRequestURI();

        List<String> excludePathPatterns = authConfigAdapter.excludePathPatterns();

        AntPathMatcher pathMatcher = new AntPathMatcher();
        // 如果匹配不需要授权的路径，就不需要校验是否需要授权
        if (CollectionUtil.isNotEmpty(excludePathPatterns)) {
            for (String excludePathPattern : excludePathPatterns) {
                if (pathMatcher.match(excludePathPattern, requestUri)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }

        String accessToken = request.getHeader("Authorization");
        // 也许需要登录，不登陆也能用的uri
        boolean mayAuth = pathMatcher.match(AuthConfigAdapter.MAYBE_AUTH_URI, requestUri);

        UserInfoInTokenBO userInfoInToken = null;

        try {
            // 如果有token，就要获取token
            if (StrUtil.isNotBlank(accessToken)) {
                userInfoInToken = tokenStore.getUserInfoByAccessToken(accessToken, true);
            } else if (!mayAuth) {
                //返回前端401
                httpHandler.printServerResponseToWeb(ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED));
                return;
            }
            //保存上下文
            AuthUserContext.set(userInfoInToken);
            //放行
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // 手动捕获下非controller异常
            if (e instanceof WillShopBindException) {
                httpHandler.printServerResponseToWeb((WillShopBindException) e);
            } else {
                throw e;
            }
        } finally {
            AuthUserContext.clean();
        }

    }

}

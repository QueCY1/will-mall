package com.will.shop.sys.aspect;

import cn.hutool.core.date.SystemClock;
import com.will.shop.common.util.IpHelper;
import com.will.shop.common.util.Json;
import com.will.shop.security.admin.util.SecurityUtils;
import com.will.shop.sys.model.SysLog;
import com.will.shop.sys.service.SysLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 基于aspectj实现AOP切面编程，对每个增删改操作进行日志通知
 * @author will
 */
@Aspect
@Component
@RequiredArgsConstructor
public class SysLogAspect {

    private final SysLogService sysLogService;

    private static final Logger logger = LoggerFactory.getLogger(SysLogAspect.class);

    @Around("@annotation(sysLog)")
    public Object around(ProceedingJoinPoint joinPoint, com.will.shop.common.annotation.SysLog sysLog) throws Throwable {
        long startTime = SystemClock.now();
        //执行方法
        Object result = joinPoint.proceed();
        //执行时长(毫秒)
        long time = SystemClock.now() - startTime;

        //创建日志
        SysLog sysLogEntity = new SysLog();
        if(sysLog != null){
            //注解上的描述
            sysLogEntity.setOperation(sysLog.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        sysLogEntity.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        String params = Json.toJsonString(args[0]);
        sysLogEntity.setParams(params);

        //设置IP地址
        sysLogEntity.setIp(IpHelper.getIpAddr());

        //用户名
        String username = SecurityUtils.getSysUser().getUsername();
        sysLogEntity.setUsername(username);

        sysLogEntity.setTime(time);
        sysLogEntity.setCreateDate(new Date());
        //保存系统日志
        sysLogService.save(sysLogEntity);

        return result;
    }

}

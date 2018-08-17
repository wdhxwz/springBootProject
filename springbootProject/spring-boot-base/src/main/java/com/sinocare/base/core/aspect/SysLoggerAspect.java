package com.sinocare.base.core.aspect;

import com.alibaba.fastjson.JSON;
import com.krista.spring.boot.dto.annotation.SysLogger;
import com.krista.spring.boot.model.SysLog;
import com.krista.spring.boot.model.SysUser;
import com.krista.spring.boot.service.SysLogService;
import com.sinocare.base.core.util.HttpContextUtils;
import com.sinocare.base.core.util.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Description: spring-boot-base
 */
@Aspect
@Component
@Slf4j
public class SysLoggerAspect {

    @Resource
    private SysLogService sysLogService;

    // 切点（注解为切点）
    @Pointcut("@annotation(com.krista.spring.boot.dto.annotation.SysLogger)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        new SaveSysLogThread(point, time).run();

        return result;
    }

    public class SaveSysLogThread implements Runnable{
        private ProceedingJoinPoint joinPoint;
        private long time;
        public SaveSysLogThread(ProceedingJoinPoint joinPoint, long time){
            this.joinPoint = joinPoint;
            this.time = time;
        }

        @Override
        public void run() {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            SysLog sysLog = new SysLog();
            SysLogger sysLogger = method.getAnnotation(SysLogger.class);
            if (sysLogger != null) {
                //注解上的描述
                sysLog.setOperation(sysLogger.value());
            }

            // 请求的方法名
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = signature.getName();
            sysLog.setMethod(className + "." + methodName + "()");

            // 请求的参数
            Object[] args = joinPoint.getArgs();
            try {
                if (args.length > 0) {
                    String params = JSON.toJSONString(args[0]);
                    sysLog.setParams(params);
                }
            } catch (Exception e) {
                log.error("@SysLogger toJSONString error {}", e);
            }

            // 获取request
            HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
            // 获取IP地址
            sysLog.setIp(IPUtils.getIpAddr(request));

            // 用户名
            try {
                // 登录失效时获取到的用户信息是null
                SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
                if (sysUser != null) {
                    String username = sysUser.getUsername();
                    sysLog.setUsername(username);
                } else {
                    sysLog.setUsername("获取不到用户信息");
                }
            } catch (Exception e) {
                log.error("@SysLogger get username error {}", e);
            }

            sysLog.setTime(time);
            sysLog.setCreateDate(new Date());
            //保存系统日志
            sysLogService.save(sysLog);
        }
    }
}

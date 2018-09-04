package com.krista.spring.boot.base.core.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: spring-boot-base
 * Created by jeikerxiao on 2018/6/27 下午5:32
 */
public class HttpContextUtils {

    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // TODO 这里怎么会获取到null呢？
        // 和request不在同一条线程，导致请求destroy掉之后，与该线程相关的缓存数据也被remove掉了
        if(servletRequestAttributes != null){
            return servletRequestAttributes.getRequest();
        }

        return null;
    }

    public static String getDomain(){
        HttpServletRequest request = getHttpServletRequest();
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
    }

    public static String getOrigin(){
        HttpServletRequest request = getHttpServletRequest();
        return request.getHeader("Origin");
    }

    public static String getIp(){
        HttpServletRequest request = getHttpServletRequest();

        return IPUtils.getIpAddr(request);
    }
}

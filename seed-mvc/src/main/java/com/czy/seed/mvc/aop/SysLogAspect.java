package com.czy.seed.mvc.aop;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.czy.seed.mvc.annotation.AutoLog;
import com.czy.seed.mvc.sys.entity.SysLog;
import com.czy.seed.mvc.sys.service.SysLogService;
import com.czy.seed.mvc.util.PrincipalUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * 系统日志，切面处理类
 * <p>
 * Created by 003914[panlc] on 2017-06-12.
 */
@Aspect
@Component
public class SysLogAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysLogService sysLogService;

    @Pointcut("@annotation(com.czy.seed.mvc.annotation.AutoLog)")
    public void logPointCut() {

    }

    @Before("logPointCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLog sysLog = new SysLog();
        sysLog.setOpeTime(new Date());
        sysLog.setOpeId(PrincipalUtil.getLoginUser().getId());

//		解析注解
        AutoLog autoLog = method.getAnnotation(AutoLog.class);
        if (autoLog != null) {
            //注解上的描述
            sysLog.setOperation(autoLog.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");
        //请求的参数
        Object[] args = joinPoint.getArgs();
        String params = JSON.toJSONString(args[0]);
        sysLog.setParams(params);
        //获取request
        HttpServletRequest request = getHttpServletRequest();
        //设置IP地址
        sysLog.setIp(getIpAddr(request));

        //保存系统日志
        sysLogService.insert(sysLog);
    }

    /**
     * 获取servletRequest
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取IP地址
     * <p>
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，
     * X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            logger.error("occurred while get user's ip address ", e);
        }

        //使用代理，则获取第一个IP地址
//        if(StringUtils.isEmpty(ip) && ip.length() > 15) {
//			if(ip.indexOf(",") > 0) {
//				ip = ip.substring(0, ip.indexOf(","));
//			}
//		}

        return ip;
    }
}

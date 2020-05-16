package com.fgy.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


/**
 * 日志的记录
 * AOP切面
 */

@Aspect
@Component
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.fgy.web.*.*(..))")
    public void log() {

    }
    /*记录请求*/
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        /*获取HttpRequest,以此获取url以及ip*/
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url=request.getRequestURL().toString();
        String ip=request.getRemoteAddr();
        /*依靠传入的切面的对象获取类名以及方法名*/
        String classMethod = joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        Object[] args=joinPoint.getArgs();
        Requestlog requestlog=new Requestlog(url,ip,classMethod,args);
        logger.info("Request : {}",requestlog.toString());
    }

    @After("log()")
    public void daAfter() {
        logger.info("-----------doAfter-----------------");
    }

    @AfterReturning(returning = "result", pointcut = "log()")
    public void daAfterReturn(Object result) {
        logger.info("Result : {}", result);
    }
    /*日志实体类*/
    private class Requestlog{
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public Requestlog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "Requestlog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}

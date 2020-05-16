package com.fgy.interceptor;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Web配置类
 *配置拦截的路径
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    /*.addPathPatterns("") 添加拦截路径*/
    /*.excludePathPatterns("") 添加忽略路径*/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin")
                .excludePathPatterns("/admin/login");
    }
}

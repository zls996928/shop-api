package com.fh.shop.api.Config;

import com.fh.shop.api.interceptor.IdempotentInterceptor;
import com.fh.shop.api.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//等同于xml中的beans根标签
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {
    // 多个拦截器组成一个拦截器链
    // addPathPatterns 用于添加拦截规则
    // excludePathPatterns 用户排除拦截

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())//添加拦截器
                .addPathPatterns("/**") //拦截所有请求
                .excludePathPatterns("/members/login", "/members/add");//对应的不拦截的请求

        registry.addInterceptor(new IdempotentInterceptor())//添加拦截器
                .addPathPatterns("/**"); //拦截所有请求

    }

    //<bean id="loginInterceptor" class="com.fh.shop.api.interceptor.LoginInterceptor"></bean>
//    @Bean
//    public LoginInterceptor loginInterceptor(){
//        return new LoginInterceptor();
//    }
}

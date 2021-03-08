package com.cheng.oauth2resourcepractice.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * filter 配置
 *
 * @author CTPlayer
 **/
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new JwtTokenFilter());
        registration.addUrlPatterns("/*");
        registration.setName("JWT token filter");
        registration.setOrder(1);
        return registration;
    }
}

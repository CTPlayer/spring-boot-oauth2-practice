package com.cheng.springbootoauth2practice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Spring Security 的配置
 *
 * @author CTPlayer
 **/
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        // 此处密码前{}中代表加密方式，id可以是bcrypt、sha256等。也就是说，程序拿到传过来的密码的时候，会首先查找被“{”和“}”包括起来的id，
        // 来确定后面的密码是被怎么样加密的，如果找不到就认为id是null。
        inMemoryUserDetailsManager.createUser(User.withUsername("admin").password("{noop}admin").roles("USER",
                "ADMIN").build());
        inMemoryUserDetailsManager.createUser(User.withUsername("user01").password("{noop}user01").roles("USER").build());
        return inMemoryUserDetailsManager;
    }

    // grant_type = "password"的必须配置 https://stackoverflow.com/questions/52194081/spring-boot-oauth-unsupported-grant-type
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

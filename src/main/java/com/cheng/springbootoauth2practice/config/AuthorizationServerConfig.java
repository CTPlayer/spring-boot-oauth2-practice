package com.cheng.springbootoauth2practice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 授权配置
 *
 * @author CTPlayer
 **/
@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("client01")
                // 此处密码前{}中代表加密方式，id可以是bcrypt、sha256等。也就是说，程序拿到传过来的密码的时候，会首先查找被“{”和“}”包括起来的id，
                // 来确定后面的密码是被怎么样加密的，如果找不到就认为id是null。
                .secret("{noop}1234")
                .authorizedGrantTypes("password", "refresh_token")
                // The scope to which the client is limited. If scope is undefined or empty (the default) the client is not limited by scope.
                // 用来限制客户端权限访问的范围，可以用来设置角色或者权限，也可以不设置
                // 虽然官方网站说是服务器端的client配置scopes可以为空，但是经过实际操作及跟踪源码来看password模式下调用/oauth/token端点拿用户token信息服务端可以为空，
                // 但是客户端必须传 scopes。refresh_token 模式服务端及 client 端的 scopes 都需要配置，所以即使我们用不到 scopes 前后端最好都配置上scopes(“all”)。
                .scopes("all");
    }

    // grant_type = "password"的必须配置 https://stackoverflow.com/questions/52194081/spring-boot-oauth-unsupported-grant-type
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore()).accessTokenConverter(tokenEnhancer());
    }

    // 生成 token 时指定签名密钥
    @Bean
    public JwtAccessTokenConverter tokenEnhancer() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        Resource resource = new ClassPathResource("myPrivate.key");
        try(InputStream in = resource.getInputStream()) {
            String privateKey = StreamUtils.copyToString(in, StandardCharsets.UTF_8);
            jwtAccessTokenConverter.setSigningKey(privateKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jwtAccessTokenConverter;
    }

    // The JSON Web Token (JWT) version of the store encodes all the data about the grant into the token itself.
    // The JwtTokenStore is not really a "store" in the sense that it doesn't persist any data,
    // but it plays the same role of translating between token values and authentication information in the DefaultTokenServices.
    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(tokenEnhancer());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }
}

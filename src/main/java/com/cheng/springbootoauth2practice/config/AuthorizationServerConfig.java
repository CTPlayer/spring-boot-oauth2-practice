package com.cheng.springbootoauth2practice.config;

import com.cheng.springbootoauth2practice.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

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

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JWTTokenEnhancer tokenEnhancer;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("client01")
                // 此处密码前{}中代表加密方式，id可以是bcrypt、sha256等。也就是说，程序拿到传过来的密码的时候，会首先查找被“{”和“}”包括起来的id，
                // 来确定后面的密码是被怎么样加密的，如果找不到就认为id是null。
                // 此处也要加密，否则会报 Encoded password does not look like bcrypt 错误
                .secret(encoder.encode("1234"))
                .authorizedGrantTypes("password", "refresh_token")
                // 设置access_token的有效时间(秒),默认(60 * 60 * 12,12小时)
                .accessTokenValiditySeconds(100)
                // 设置refresh_token有效期(秒)，默认(60 *60 * 24 * 30, 30天)
                .refreshTokenValiditySeconds(1000)
                // The scope to which the client is limited. If scope is undefined or empty (the default) the client is not limited by scope.
                // 用来限制客户端权限访问的范围，可以用来设置角色或者权限，也可以不设置
                // 虽然官方网站说是服务器端的client配置scopes可以为空，但是经过实际操作及跟踪源码来看password模式下调用/oauth/token端点拿用户token信息服务端可以为空，
                // 但是客户端必须传 scopes。refresh_token 模式服务端及 client 端的 scopes 都需要配置，所以即使我们用不到 scopes 前后端最好都配置上scopes(“all”)。
                .scopes("all");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer, tokenConverter()));

        endpoints.authenticationManager(authenticationManager) // grant_type = "password"的必须配置 https://stackoverflow.com/questions/52194081/spring-boot-oauth-unsupported-grant-type
                .tokenStore(tokenStore()).accessTokenConverter(tokenConverter())
                .tokenEnhancer(tokenEnhancerChain);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    // 生成 token 时指定签名密钥
    @Bean
    public JwtAccessTokenConverter tokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
//        Resource resource = new ClassPathResource("myPrivate.key");
//        try(InputStream in = resource.getInputStream()) {
//            String privateKey = StreamUtils.copyToString(in, StandardCharsets.UTF_8);
//            jwtAccessTokenConverter.setSigningKey(privateKey);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        jwtAccessTokenConverter.setKeyPair(CommonUtil.generateKeyPair());
        return jwtAccessTokenConverter;
    }

    // The JSON Web Token (JWT) version of the store encodes all the data about the grant into the token itself.
    // The JwtTokenStore is not really a "store" in the sense that it doesn't persist any data,
    // but it plays the same role of translating between token values and authentication information in the DefaultTokenServices.
    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(tokenConverter());
    }

    @Bean
    public JWTTokenEnhancer tokenEnhancer() {
        return new JWTTokenEnhancer();
    }
}

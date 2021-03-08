package com.cheng.oauth2resourcepractice.config;

import com.cheng.oauth2resourcepractice.CommonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 资源配置
 *
 * @author CTPlayer
 **/
@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    // 验证 token 时指定校验密钥
    @Bean
    public JwtAccessTokenConverter tokenEnhancer() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
//        Resource resource = new ClassPathResource("myPrivate.key");
//        try(InputStream in = resource.getInputStream()) {
//            String privateKey = StreamUtils.copyToString(in, StandardCharsets.UTF_8);
//            jwtAccessTokenConverter.setVerifierKey(privateKey);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        jwtAccessTokenConverter.setVerifierKey(
            "-----BEGIN PUBLIC KEY-----\n" +
            CommonUtil.PUBLIC_KEY +
            "\n-----END PUBLIC KEY-----"
        );
        return jwtAccessTokenConverter;
    }

    // The Resource Server also needs to be able to decode the tokens so the JwtTokenStore has a dependency on a JwtAccessTokenConverter,
    // and the same implementation is needed by both the Authorization Server and the Resource Server.
    // The tokens are signed by default, and the Resource Server also has to be able to verify the signature,
    // so it either needs the same symmetric (signing) key as the Authorization Server (shared secret, or symmetric key),
    // or it needs the public key (verifier key) that matches the private key (signing key) in the Authorization Server (public-private or asymmetric key).
    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(tokenEnhancer());
    }
}

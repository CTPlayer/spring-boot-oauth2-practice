package com.cheng.oauth2resourcepractice.filter;

import com.cheng.oauth2resourcepractice.CommonUtil;
import io.jsonwebtoken.Claims;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 解析 token
 *
 * @author CTPlayer
 **/
public class JwtTokenFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");
        if (authorization != null) {
            String token = authorization.replace("bearer", "").replace("/n", "");
            Claims claims = CommonUtil.parseJwt(token);
            if (claims != null) {
                System.out.println("token 作者为：" + claims.get("author"));
            }
        }
        chain.doFilter(httpServletRequest, response);
    }
}

package com.cheng.springbootoauth2practice.service;

import com.cheng.springbootoauth2practice.dao.OAuthDao;
import com.cheng.springbootoauth2practice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * OAuth2 加载用户信息
 *
 * @author CTPlayer
 **/
@Service
public class CustomDetailsService implements UserDetailsService {

    @Autowired
    private OAuthDao oAuthDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = oAuthDao.getUserDetails(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }
        return user;
    }
}

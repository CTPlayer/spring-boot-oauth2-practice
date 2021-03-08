package com.cheng.springbootoauth2practice.dao;

import com.cheng.springbootoauth2practice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 数据库操作
 *
 * @author CTPlayer
 **/
@Repository
public class OAuthDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User getUserDetails(String username) {
        Collection<GrantedAuthority> grantedAuthoritiesList = new ArrayList<>();
        String userSQLQuery = "SELECT * FROM USERS WHERE USERNAME = ?";
        List<User> list = jdbcTemplate.query(userSQLQuery, new String[]{ username },
                (ResultSet rs, int rowNum) -> {
                    User user = new User();
                    user.setUsername(rs.getString("USERNAME"));
                    user.setPassword(rs.getString("PASSWORD"));
                    return user;
                });
        if (!list.isEmpty()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_SYSTEMADMIN");
            grantedAuthoritiesList.add(grantedAuthority);
            list.get(0).setAuthorities(grantedAuthoritiesList);
            return list.get(0);
        }
        return null;
    }
}

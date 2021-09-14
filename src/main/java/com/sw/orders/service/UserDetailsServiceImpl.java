package com.sw.orders.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.orders.component.Auths;
import com.sw.orders.component.Roles;
import com.sw.orders.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * spring scurity 用户验证
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private Auths auths;
    @Autowired
    private Roles roles;
//    @Autowired
//    private AuthsMapper authsMapper;
    @Autowired
    private HttpSession session;

    /**
     * 用户认证逻辑
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        QueryWrapper qe = new QueryWrapper();
        qe.eq("username", username);
        User user = userService.getOne(qe);
        if(user == null) {
            throw new UsernameNotFoundException("");
        }

        session.setAttribute("user", user);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // 获取用户的角色id，再遍历角色id对应的权限id列表
        for(String auth : roles.getRoleMap().get(user.getRoleId()).getAuthList().split(",")) {
            // 把用户权限id映射为权限名
           authorities.add(new SimpleGrantedAuthority(auths.getAuthMap().get(Integer.parseInt(auth)).getAuthName()));
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities).build();
    }


}


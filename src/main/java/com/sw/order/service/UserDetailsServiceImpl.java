package com.sw.order.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.order.component.Auths;
import com.sw.order.component.Roles;
import com.sw.order.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * spring scurity 用户验证
 */
@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper qe = new QueryWrapper();
        qe.eq("username", username);
        User user = userService.getOne(qe);
        if(user == null) {
            throw new UsernameNotFoundException("用户名没找到");
        }
        session.setAttribute("user", user);
//        authsMapper.selectList(null);



        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // 获取用户的角色id，再遍历角色id对应的权限id列表
        for(String auth : roles.getRoleMap().get(user.getRoleId()).getAuthList().split(",")) {
            // 把用户权限id映射为权限名
           authorities.add(new SimpleGrantedAuthority(auths.getAuthMap().get(Integer.parseInt(auth)).getAuthName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }


}


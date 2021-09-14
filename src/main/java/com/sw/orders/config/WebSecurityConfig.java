package com.sw.orders.config;

import com.sw.orders.config.encode.CustomPasswordEncoder;
import com.sw.orders.config.filter.CustomAuthenticationFilter;
import com.sw.orders.config.handler.LoginFailureHandler;
import com.sw.orders.config.handler.LoginOutSuccessHandler;
import com.sw.orders.config.handler.LoginSuccessHandler;
import com.sw.orders.entity.Result;
import com.sw.orders.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * spring security 的基本配置
 * @EnableWebSecurity 开启spring security
 * @EnableGlobalMethodSecurity(proxyTargetClass = true,prePostEnabled = true,securedEnabled = true) 启动角色拦截
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(proxyTargetClass = true,prePostEnabled = true,securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginOutSuccessHandler loginOutSuccessHandler;
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private LoginFailureHandler loginFailureHandler;

    /**
     * configure(HttpSecurity)方法定义了哪些URL路径应该被保护
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//    http.authorizeRequests()
//            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll();//让Spring security放行所有preflight request
        http.csrf().disable().cors();
        http.authorizeRequests()
            // “/”和“/ home”路径被配置为不需要任何身份验证。所有其他路径必须经过身份验证。
            .antMatchers("/register.do", "/", "/index").permitAll()
            .anyRequest().authenticated()
//            .and()
//            .formLogin()
//            .loginProcessingUrl("/login.do")
//                .successHandler(loginSuccessHandler)
//                .failureHandler(loginFailureHandler)
//                .permitAll()
            .and()
                .logout()
                .logoutSuccessHandler(loginOutSuccessHandler)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(new AuthenticationEntryPoint() {
                @Override
                public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException authException) throws IOException {
                    // 关闭未认证重定向
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    Result result = Result.clientError();
                    if (authException instanceof InsufficientAuthenticationException) {
                        result.setCode(736);
                        result.setMsg("请登录!");
                    }
                    out.write(result.toJson());
                    out.flush();
                    out.close();
                }
            }).and()
            .rememberMe()
            .userDetailsService(getUserDetailsService())
            .tokenValiditySeconds(60 * 60);

        //用重写的Filter替换掉原有的UsernamePasswordAuthenticationFilter
        http.addFilterAt(customAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/css/**","/fonts/**", "/js/**");
    }

    //注册自定义的UsernamePasswordAuthenticationFilter
    @Bean
    CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
        filter.setAuthenticationFailureHandler(new LoginFailureHandler());
        filter.setFilterProcessesUrl("/login.do");

        //这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，不然要自己组装AuthenticationManager
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    /**
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
        auth.userDetailsService(getUserDetailsService()).passwordEncoder(getCustomPassword());
    }

    @Bean
    public PasswordEncoder getCustomPassword() {
        return new CustomPasswordEncoder();
    }

    //    @Autowired
    @Bean
    public UserDetailsService getUserDetailsService() {
        return new UserDetailsServiceImpl();
    }
}


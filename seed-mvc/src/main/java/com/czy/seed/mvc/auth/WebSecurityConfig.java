package com.czy.seed.mvc.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 权限认证
 * Created by panlc on 2017-05-22.
 */

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SysUserDetailsService sysUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/charge/order/index","/cki/orderConfirm/orderConfirmation","/cki/Log/select").not().authenticated() //任何请求,登录后可以访问
                .and().formLogin().loginPage("/login").permitAll()
                .and().logout().permitAll()
                .and().csrf().disable()
                .headers().frameOptions().disable()
                .and().exceptionHandling().authenticationEntryPoint(ajaxAuthenticationEntryPoint());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(sysUserDetailsService);
    }

    /**
     * 取消common目录下的访问权限控制
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/lib/**","/charge/*");
    }

    /**
     * 设置加密器
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        指定密码加密所使用的加密器为passwordEncoder()
//        需要将密码加密后写入数据库
        auth.userDetailsService(sysUserDetailsService).passwordEncoder(passwordEncoder());
        auth.eraseCredentials(false);
    }

    /**
     * 设置用户密码的加密方式为MD5加密
     *
     * @return
     */
    @Bean
    public Md5PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder();
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    public AjaxAuthenticationEntryPoint ajaxAuthenticationEntryPoint() {
        AjaxAuthenticationEntryPoint point = new AjaxAuthenticationEntryPoint("/login");
        return point;
    }

}

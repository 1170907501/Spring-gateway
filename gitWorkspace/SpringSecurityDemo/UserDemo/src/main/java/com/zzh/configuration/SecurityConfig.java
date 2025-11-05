package com.zzh.configuration;

import com.zzh.filter.JwtAuthenticationTokenFilter;
import com.zzh.filter.JwtRefreshFilter;
import com.zzh.handler.LoginFailureHandler;
import com.zzh.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

@Configuration
@EnableWebSecurity  // 开启spring sercurity支持
public class SecurityConfig {

    @Autowired // 字段注入
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired // 字段注入
    private JwtRefreshFilter jwtRefreshFilter;

    /**
     * 配置用户信息
     * @return
     */

    @Bean
    public UserDetailsService userDetailsService() {
        // 使用默认加密方式bcrypt对密码进行加密，添加用户信息
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("jay")
                .password("123456")
                .roles("user")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password("{noop}123456") // 对密码不加密
                .roles("admin", "user")
                .build();

        UserDetails user1 = User.withUsername("user1")
                .password("{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG")
                .roles("USER")
                .build();
        UserDetails admin1 = User.withUsername("admin1")
                .password("{noop}123456") // noop表示对密码不加密
                .roles("admin", "user")
                .build();
        UserDetails admin3 = User.withUsername("admin3")
                // 指定加密算法对密码加密
                .password(passwordEncoder().encode("123456"))
                .roles("admin", "user")
                .build();

        return new InMemoryUserDetailsManager(user, admin,user1,admin1,admin3);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //return NoOpPasswordEncoder.getInstance();  // 不加密
        return new BCryptPasswordEncoder(); // 加密方式bcrypt
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 表单提交
        http.formLogin((formLogin) -> formLogin
                .loginPage("/login.html") // 指定自定义登录页面地址
                .loginProcessingUrl("/user/login") // 登录访问路径：前台界面提交表单之后跳转到这个路径进行UserDetailsService的验证，必须和表单提交接口一样
                .defaultSuccessUrl("/admin/demo") // 认证成功之后跳转的路径
                .successHandler(new LoginSuccessHandler())
                .failureHandler(new LoginFailureHandler())

        )// 添加JWT登录过滤器，在登录之前获取token并校验
        .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(jwtRefreshFilter, JwtAuthenticationTokenFilter.class); // 确保JWT过滤器已注册顺序

        // 对请求进行访问控制设置
        /*http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                // 设置哪些路径可以直接访问，不需要认证
                .requestMatchers("/login.html","/user/login").permitAll()
                .anyRequest().authenticated() // 其他路径的请求都需要认证
        );*/


        http.authorizeHttpRequests(auth -> auth
                // 使用AntPathRequestMatcher包装路径
                .requestMatchers(
                        new AntPathRequestMatcher("/login.html"),
                        new AntPathRequestMatcher("/user/login")
                ).permitAll()
                .anyRequest().authenticated()
        );
        // 关闭跨站点请求伪造csrf防护
        http.csrf((csrf) -> csrf.disable());

        return http.build();
    }

    // 3. 配置DaoAuthenticationProvider
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // 4. 配置AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(daoAuthenticationProvider()); // 注入自定义Provider
    }
}


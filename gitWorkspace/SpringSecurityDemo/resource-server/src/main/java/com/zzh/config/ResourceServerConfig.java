package com.zzh.config;

import com.zzh.exception.MyAuthenticationEntryPoint;
import com.zzh.handler.MyAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class ResourceServerConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                //所有的访问都需要通过身份认证
                .anyRequest().authenticated()
        )
                .oauth2ResourceServer((oauth2ResourceServer) -> oauth2ResourceServer
                        .jwt(Customizer.withDefaults())

                );

        http
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                        .accessDeniedHandler(new MyAccessDeniedHandler())
                );

        return http.build();
    }

}
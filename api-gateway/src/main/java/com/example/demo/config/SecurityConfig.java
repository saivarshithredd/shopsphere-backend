package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // ❌ Disable default auth completely
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)

                .authorizeExchange(ex -> ex

                        // ✅ Allow ALL services through gateway
                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers("/catalog/**").permitAll()
                        .pathMatchers("/orders/**").permitAll()

                        // ✅ Swagger
                        .pathMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // 🔥 VERY IMPORTANT: allow everything else
                        .anyExchange().permitAll()
                )

                .build();
    }
}
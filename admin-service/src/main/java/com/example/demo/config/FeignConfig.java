package com.example.demo.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.getCredentials() != null) {
                String token = auth.getCredentials().toString();
                System.out.println("FEIGN FORWARDING TOKEN: " + token.substring(0, 20) + "...");
                template.header("Authorization", "Bearer " + token);
            } else {
                System.out.println("FEIGN: No token found in SecurityContext!");
            }
        };
    }
}
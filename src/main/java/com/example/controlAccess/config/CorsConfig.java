package com.example.controlAccess.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/auth/**")
                .allowedOrigins("http://localhost:5173")  // Permite o frontend acessando a API
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Content-Type", "Authorization")
                .allowCredentials(true);  // Permite credenciais como cookies ou tokens

        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")  // Permite o frontend acessando a API
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Content-Type", "Authorization")
                .allowCredentials(true);  // Permite credenciais como cookies ou tokens
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        return request -> new org.springframework.web.cors.CorsConfiguration().applyPermitDefaultValues();
    }
}

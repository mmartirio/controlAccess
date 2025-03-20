package com.example.controlAccess.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Configuração para a rota "/auth/**"
        registry.addMapping("/auth/**")
                .allowedOrigins("http://localhost:5173")  // Permite o frontend acessando a API
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Permite os métodos necessários
                .allowedHeaders("Content-Type", "Authorization")
                .allowCredentials(true);  // Permite credenciais como cookies ou tokens

        // Configuração para a rota "/api/**"
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")  // Permite o frontend acessando a API
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Permite os métodos necessários
                .allowedHeaders("Content-Type", "Authorization")
                .allowCredentials(true);  // Permite credenciais como cookies ou tokens
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
            configuration.applyPermitDefaultValues();
            return configuration;
        };
    }
}


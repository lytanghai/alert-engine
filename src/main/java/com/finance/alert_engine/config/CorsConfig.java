package com.finance.alert_engine.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply to all endpoints
                .allowedOrigins("https://byte-income.onrender.com","http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow all methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true) // Allow cookies/auth headers
                .maxAge(3600); // Cache preflight response for 1 hour
    }
}
package com.apptechnosoft.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/notifications/stream")
                .allowedOrigins("http://localhost:1111")  // Replace with your frontend URL
                .allowedMethods("GET");
    }
}
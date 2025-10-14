package com.example.deploy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                    "http://localhost:*",                 // Qualquer localhost com qualquer porta
                    "http://127.0.0.1:*",                 // Loopback por IP
                    "http://[::1]:*",                // IPv6 loopback
                    "http://192.168.*:*",                // Rede local (ex.: 192.168.x.x)
                    "https://staging.proweb.leoproti.com.br:8020",  // Staging
                    "https://proweb.leoproti.com.br:8021",          // Production
                    "https://*.vercel.app",              // Front-ends hospedados no Vercel
                    "https://*.vercel.app/",             // variação com barra
                    "http://*.vercel.app"                // caso use http (dev)
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
package com.example.deploy.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 🌐 WebConfig - Configuração Web e OpenAPI/Swagger
 * 
 * Configura CORS e documentação da API com suporte a JWT
 * 
 * @author Leonardo Vieira Guimarães
 * @since 2025-09-23
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Sistema de Autenticação JWT",
        version = "v2.1.0",
        description = "API REST com Spring Security + JWT para autenticação e autorização",
        contact = @Contact(
            name = "Leonardo Vieira Guimarães",
            email = "leonardo@exemplo.com",
            url = "https://github.com/LeonardoVieiraGuimaraes"
        )
    )
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    description = "Token JWT obtido através do endpoint /auth/login"
)
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:8022",           // Dev local
                    "https://staging.proweb.leoproti.com.br:8020",  // Staging
                    "https://proweb.leoproti.com.br:8021"           // Production
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
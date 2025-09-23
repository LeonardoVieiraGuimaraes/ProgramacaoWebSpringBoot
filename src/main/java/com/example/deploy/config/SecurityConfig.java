package com.example.deploy.config;

import com.example.deploy.security.JwtAuthenticationFilter;
import com.example.deploy.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * 🔐 Security Configuration - Configuração de segurança da aplicação
 * 
 * Configura:
 * - Autenticação JWT
 * - Autorização baseada em roles
 * - CORS
 * - Endpoints públicos e protegidos
 * - Criptografia de senhas
 * 
 * @author Leonardo Vieira Guimarães
 * @since 2025-09-23
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 🔒 Configuração da cadeia de filtros de segurança
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 🚫 Desabilitar CSRF (não necessário para APIs stateless)
            .csrf(AbstractHttpConfigurer::disable)
            
            // 🌐 Configurar CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 📋 Configurar autorização de requisições
            .authorizeHttpRequests(authz -> authz
                // 🌍 Endpoints públicos (sem autenticação)
                .requestMatchers(
                    "/",
                    "/auth/**",
                    "/public/**",
                    "/actuator/health",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/favicon.ico",
                    "/error"
                ).permitAll()
                
                // 👑 Endpoints administrativos (apenas ADMIN)
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // 👨‍💼 Endpoints de gestão (ADMIN ou MANAGER)
                .requestMatchers("/management/**").hasAnyRole("ADMIN", "MANAGER")
                
                // 📊 Endpoints de API (usuários autenticados)
                .requestMatchers("/api/**").authenticated()
                
                // 🔒 Todas as outras requisições precisam de autenticação
                .anyRequest().authenticated()
            )
            
            // 🚫 Desabilitar sessões (stateless com JWT)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // 🔐 Configurar provider de autenticação
            .authenticationProvider(authenticationProvider())
            
            // 🛡️ Adicionar filtro JWT antes do filtro de autenticação padrão
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 🔐 Configuração do provider de autenticação
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * 🔑 Manager de autenticação
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * 🔒 Encoder de senhas (BCrypt)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Força 12 para maior segurança
    }

    /**
     * 🌐 Configuração CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 🌍 Origens permitidas (ajustar conforme necessário)
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:*",
            "https://localhost:*",
            "http://127.0.0.1:*",
            "https://127.0.0.1:*"
        ));
        
        // 📋 Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));
        
        // 📋 Headers permitidos
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        
        // 📋 Headers expostos
        configuration.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials",
            "Authorization"
        ));
        
        // 🍪 Permitir credenciais
        configuration.setAllowCredentials(true);
        
        // ⏰ Cache do preflight
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
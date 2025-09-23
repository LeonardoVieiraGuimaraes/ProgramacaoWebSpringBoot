package com.example.deploy.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 🛡️ JWT Authentication Filter - Filtro de autenticação JWT
 * 
 * Intercepta todas as requisições HTTP e:
 * 1. Extrai o token JWT do header Authorization
 * 2. Valida o token
 * 3. Autentica o usuário no SecurityContext
 * 
 * @author Leonardo Vieira Guimarães
 * @since 2025-09-23
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                    @NonNull HttpServletResponse response, 
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // 🔍 Extrair token JWT da requisição
            String jwt = getJwtFromRequest(request);
            
            if (StringUtils.hasText(jwt) && jwtTokenUtil.validateToken(jwt)) {
                // 📝 Extrair username do token
                String username = jwtTokenUtil.getUsernameFromToken(jwt);
                
                // 👤 Carregar detalhes do usuário
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // ✅ Validar token com os detalhes do usuário
                if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                    // 🔐 Criar autenticação
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails, 
                            null, 
                            userDetails.getAuthorities()
                        );
                    
                    // 📍 Adicionar detalhes da requisição
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 🔒 Definir autenticação no SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    // 📊 Log para debug (opcional)
                    logger.debug("Usuário autenticado: " + username);
                }
            }
        } catch (Exception ex) {
            // 🚨 Log do erro sem interromper o fluxo
            logger.error("Não foi possível definir autenticação do usuário no security context", ex);
        }
        
        // ➡️ Continuar com o próximo filtro
        filterChain.doFilter(request, response);
    }

    /**
     * 🔍 Extrair token JWT do header Authorization
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_STRING);
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        
        return null;
    }

    /**
     * 🚫 Pular filtro para certas URLs (opcional)
     * Pode ser usado para otimizar performance em endpoints públicos
     */
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        
        // 🎯 Pular filtro para endpoints de autenticação e recursos públicos
        return path.startsWith("/auth/") || 
               path.startsWith("/public/") ||
               path.startsWith("/actuator/health") ||
               path.startsWith("/swagger-ui/") ||
               path.startsWith("/v3/api-docs/") ||
               path.equals("/favicon.ico");
    }
}
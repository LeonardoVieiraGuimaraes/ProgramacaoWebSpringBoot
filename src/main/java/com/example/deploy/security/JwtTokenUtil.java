package com.example.deploy.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 🔐 JWT Utility - Gerenciamento de tokens JWT
 * 
 * Responsável por:
 * - Gerar tokens JWT
 * - Validar tokens
 * - Extrair informações dos tokens
 * - Verificar expiração
 * 
 * @author Leonardo Vieira Guimarães
 * @since 2025-09-23
 */
@Component
public class JwtTokenUtil {

    // 🔑 Chave secreta para assinatura dos tokens
    private final SecretKey jwtSecret;

    // ⏰ Tempo de expiração do token (em milissegundos)
    @Value("${jwt.expiration:86400000}") // 24 horas por padrão
    private long jwtExpirationMs;

    // 🏷️ Issuer do token
    @Value("${jwt.issuer:ProgramacaoWebSpringBoot}")
    private String jwtIssuer;

    public JwtTokenUtil(@Value("${jwt.secret:defaultSecretKeyForDevelopmentOnlyDoNotUseInProduction}") String secret) {
        // 🔐 Gera uma chave segura a partir da string secreta
        this.jwtSecret = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 🎯 Gerar token JWT a partir da autenticação
     */
    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return generateTokenFromUsername(userPrincipal.getUsername());
    }

    /**
     * 🎯 Gerar token JWT a partir do username
     */
    public String generateTokenFromUsername(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * 🎯 Gerar token JWT com claims customizados
     */
    public String generateTokenWithClaims(String username, Map<String, Object> extraClaims) {
        Map<String, Object> claims = new HashMap<>(extraClaims);
        return createToken(claims, username);
    }

    /**
     * 🏗️ Criar token JWT
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuer(jwtIssuer)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(jwtSecret, Jwts.SIG.HS512)
                .compact();
    }

    /**
     * 🔍 Extrair username do token
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * ⏰ Extrair data de expiração do token
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 📅 Extrair data de criação do token
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    /**
     * 🏷️ Extrair issuer do token
     */
    public String getIssuerFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuer);
    }

    /**
     * 🔍 Extrair claim específico do token
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 📋 Extrair todos os claims do token
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * ⏰ Verificar se o token expirou
     */
    public Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * ✅ Validar token
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * ✅ Validar token (versão simples)
     */
    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(jwtSecret)
                .build()
                .parseSignedClaims(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 🔍 Verificar se o token pode ser renovado
     */
    public Boolean canTokenBeRefreshed(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 🔄 Renovar token
     */
    public String refreshToken(String token) {
        try {
            final Claims claims = getAllClaimsFromToken(token);
            String username = claims.getSubject();
            Map<String, Object> newClaims = new HashMap<>(claims);
            // Remove claims de tempo para gerar novos
            newClaims.remove("iat");
            newClaims.remove("exp");
            return createToken(newClaims, username);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * ⏰ Obter tempo restante do token (em milissegundos)
     */
    public long getTimeUntilExpiration(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration.getTime() - new Date().getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 📊 Obter informações do token para debug
     */
    public Map<String, Object> getTokenInfo(String token) {
        Map<String, Object> info = new HashMap<>();
        try {
            Claims claims = getAllClaimsFromToken(token);
            info.put("subject", claims.getSubject());
            info.put("issuer", claims.getIssuer());
            info.put("issuedAt", claims.getIssuedAt());
            info.put("expiration", claims.getExpiration());
            info.put("expired", isTokenExpired(token));
            info.put("timeUntilExpiration", getTimeUntilExpiration(token));
        } catch (Exception e) {
            info.put("error", e.getMessage());
            info.put("valid", false);
        }
        return info;
    }

    /**
     * 🔧 Getters para configuração
     */
    public long getJwtExpirationMs() {
        return jwtExpirationMs;
    }

    public String getJwtIssuer() {
        return jwtIssuer;
    }
}
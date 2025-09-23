package com.example.deploy.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 🔐 Utilitário para gerar hashes BCrypt
 * 
 * @author Leonardo Vieira Guimarães
 * @since 2025-09-23
 */
public class PasswordHashGenerator {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String password = "123456";
        String hash = encoder.encode(password);
        
        System.out.println("========================================");
        System.out.println("🔐 GERADOR DE HASH BCRYPT");
        System.out.println("========================================");
        System.out.println("Senha original: " + password);
        System.out.println("Hash BCrypt:    " + hash);
        System.out.println("========================================");
        System.out.println("Use esta hash no script SQL init-users.sql");
        System.out.println("========================================");
    }
}
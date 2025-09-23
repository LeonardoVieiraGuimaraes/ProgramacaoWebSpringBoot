package com.example.deploy.dto;

import com.example.deploy.model.Role;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 🎯 DTO para resposta de autenticação (login/refresh)
 * 
 * @author Leonardo Vieira Guimarães
 * @since 2025-09-23
 */
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private String fullName;
    private Set<Role> roles;
    private LocalDateTime expiresAt;
    private Long expiresIn; // em segundos

    // 🔧 Construtores
    public JwtResponse() {}

    public JwtResponse(String token, String username, String email, String fullName, Set<Role> roles) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.roles = roles;
    }

    public JwtResponse(String token, String username, String email, String fullName, Set<Role> roles, LocalDateTime expiresAt, Long expiresIn) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.roles = roles;
        this.expiresAt = expiresAt;
        this.expiresIn = expiresIn;
    }

    // 🔧 Getters e Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "JwtResponse{" +
                "token='[PROTECTED]'" +
                ", type='" + type + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", roles=" + roles +
                ", expiresAt=" + expiresAt +
                ", expiresIn=" + expiresIn +
                '}';
    }
}
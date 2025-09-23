package com.example.deploy.controller;

import com.example.deploy.dto.*;
import com.example.deploy.model.User;
import com.example.deploy.security.JwtTokenUtil;
import com.example.deploy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 🔐 AuthController - Controller de autenticação e autorização
 * 
 * Endpoints:
 * - POST /auth/login - Login do usuário
 * - POST /auth/signup - Registro de novo usuário
 * - POST /auth/refresh - Refresh do token JWT
 * - POST /auth/logout - Logout (invalidar token no cliente)
 * 
 * @author Leonardo Vieira Guimarães
 * @since 2025-09-23
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints de autenticação e autorização")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 🔐 Login do usuário
     */
    @PostMapping("/login")
    @Operation(summary = "Login do usuário", description = "Autentica usuário e retorna token JWT")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // 🔐 Autenticar usuário
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            // 🔒 Definir autenticação no contexto
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 🎯 Gerar token JWT
            String jwt = jwtTokenUtil.generateToken(authentication);

            // 👤 Obter detalhes do usuário
            User user = (User) authentication.getPrincipal();

            // 🔐 Atualizar último login
            userService.updateLastLogin(user.getId());

            // ⏰ Calcular expiração
            long expirationMs = jwtTokenUtil.getJwtExpirationMs();
            LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(expirationMs / 1000);
            Long expiresIn = expirationMs / 1000; // Converter para segundos

            // 📤 Criar resposta
            JwtResponse jwtResponse = new JwtResponse(
                jwt,
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRoles(),
                expiresAt,
                expiresIn
            );

            return ResponseEntity.ok(jwtResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Credenciais inválidas: " + e.getMessage()));
        }
    }

    /**
     * 📝 Registro de novo usuário
     */
    @PostMapping("/signup")
    @Operation(summary = "Registro de usuário", description = "Registra novo usuário no sistema")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            // ✅ Verificar se username já existe
            if (userService.existsByUsername(signupRequest.getUsername())) {
                return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Erro: Username já está em uso!"));
            }

            // ✅ Verificar se email já existe
            if (userService.existsByEmail(signupRequest.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Erro: Email já está em uso!"));
            }

            // 📝 Criar usuário
            User user = userService.createUser(signupRequest);

            return ResponseEntity.ok(MessageResponse.success(
                "Usuário registrado com sucesso!",
                user.getUsername()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Erro ao registrar usuário: " + e.getMessage()));
        }
    }

    /**
     * 📝 Registro rápido (apenas com dados básicos)
     */
    @PostMapping("/signup/quick")
    @Operation(summary = "Registro rápido", description = "Registra usuário com role USER padrão")
    public ResponseEntity<?> quickRegister(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // ✅ Verificar se username já existe
            if (userService.existsByUsername(loginRequest.getUsername())) {
                return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Erro: Username já está em uso!"));
            }

            // 📝 Criar usuário com role padrão
            User user = userService.createUserWithDefaultRole(
                loginRequest.getUsername(),
                loginRequest.getUsername() + "@example.com", // Email temporário
                loginRequest.getPassword()
            );

            return ResponseEntity.ok(MessageResponse.success(
                "Usuário registrado com sucesso!",
                user.getUsername()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Erro ao registrar usuário: " + e.getMessage()));
        }
    }

    /**
     * 🔄 Refresh do token JWT
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Gera novo token JWT baseado no token atual")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            // 🔍 Extrair token do header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Token JWT não fornecido!"));
            }

            String token = authHeader.substring(7);

            // ✅ Validar token atual
            if (!jwtTokenUtil.validateToken(token)) {
                return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Token JWT inválido ou expirado!"));
            }

            // 🔄 Gerar novo token
            String newToken = jwtTokenUtil.refreshToken(token);
            
            if (newToken == null) {
                return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Não foi possível renovar o token!"));
            }

            // 👤 Obter informações do usuário
            String username = jwtTokenUtil.getUsernameFromToken(token);
            User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // ⏰ Calcular nova expiração
            long expirationMs = jwtTokenUtil.getJwtExpirationMs();
            LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(expirationMs / 1000);
            Long expiresIn = expirationMs / 1000;

            // 📤 Criar resposta
            JwtResponse jwtResponse = new JwtResponse(
                newToken,
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRoles(),
                expiresAt,
                expiresIn
            );

            return ResponseEntity.ok(jwtResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Erro ao renovar token: " + e.getMessage()));
        }
    }

    /**
     * 🚪 Logout (cliente deve invalidar o token)
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Instrui cliente a invalidar o token JWT")
    public ResponseEntity<?> logout() {
        // Em uma implementação JWT stateless, o logout é responsabilidade do cliente
        // O cliente deve simplesmente descartar o token
        
        return ResponseEntity.ok(MessageResponse.success(
            "Logout realizado com sucesso! Token deve ser descartado pelo cliente."
        ));
    }

    /**
     * 📊 Verificar status do token
     */
    @GetMapping("/verify")
    @Operation(summary = "Verificar token", description = "Verifica se o token JWT é válido")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            // 🔍 Extrair token do header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Token JWT não fornecido!"));
            }

            String token = authHeader.substring(7);

            // ✅ Validar token
            if (!jwtTokenUtil.validateToken(token)) {
                return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Token JWT inválido ou expirado!"));
            }

            // 📊 Obter informações do token
            return ResponseEntity.ok(MessageResponse.success(
                "Token válido!",
                jwtTokenUtil.getTokenInfo(token)
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Erro ao verificar token: " + e.getMessage()));
        }
    }

    /**
     * 👤 Obter perfil do usuário atual
     */
    @GetMapping("/profile")
    @Operation(summary = "Perfil do usuário", description = "Retorna dados do usuário autenticado")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Usuário não autenticado!"));
            }

            User user = (User) authentication.getPrincipal();
            
            // 📤 Criar resposta sem informações sensíveis
            JwtResponse profile = new JwtResponse();
            profile.setUsername(user.getUsername());
            profile.setEmail(user.getEmail());
            profile.setFullName(user.getFullName());
            profile.setRoles(user.getRoles());

            return ResponseEntity.ok(MessageResponse.success(
                "Perfil do usuário",
                profile
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Erro ao obter perfil: " + e.getMessage()));
        }
    }
}
package com.example.deploy.controller;

import com.example.deploy.dto.MessageResponse;
import com.example.deploy.model.Role;
import com.example.deploy.model.User;
import com.example.deploy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 👥 UserManagementController - Gerenciamento de usuários (protegido)
 * 
 * Endpoints protegidos por autenticação JWT e autorização baseada em roles
 * 
 * @author Leonardo Vieira Guimarães
 * @since 2025-09-23
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Gerenciamento de Usuários", description = "Endpoints protegidos para gerenciar usuários")
@SecurityRequirement(name = "bearerAuth")
public class UserManagementController {

    @Autowired
    private UserService userService;

    /**
     * 👥 Listar todos os usuários (apenas ADMIN)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar usuários", description = "Lista todos os usuários (apenas ADMIN)")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.findAll();
            return ResponseEntity.ok(MessageResponse.success(
                "Lista de usuários recuperada com sucesso",
                users
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Erro ao listar usuários: " + e.getMessage()));
        }
    }

    /**
     * 👤 Buscar usuário por ID (ADMIN ou próprio usuário)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @Operation(summary = "Buscar usuário", description = "Busca usuário por ID (ADMIN ou próprio usuário)")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            Optional<User> user = userService.findById(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(MessageResponse.success(
                    "Usuário encontrado",
                    user.get()
                ));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Erro ao buscar usuário: " + e.getMessage()));
        }
    }

    /**
     * 👥 Listar usuários ativos (ADMIN ou MANAGER)
     */
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Listar usuários ativos", description = "Lista usuários ativos (ADMIN ou MANAGER)")
    public ResponseEntity<?> getActiveUsers() {
        try {
            List<User> users = userService.findActiveUsers();
            return ResponseEntity.ok(MessageResponse.success(
                "Lista de usuários ativos",
                users
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Erro ao listar usuários ativos: " + e.getMessage()));
        }
    }

    /**
     * 🎭 Listar usuários por role (apenas ADMIN)
     */
    @GetMapping("/by-role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Usuários por role", description = "Lista usuários por role específica (apenas ADMIN)")
    public ResponseEntity<?> getUsersByRole(@PathVariable Role role) {
        try {
            List<User> users = userService.findByRole(role);
            return ResponseEntity.ok(MessageResponse.success(
                "Usuários com role " + role.getDisplayName(),
                users
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Erro ao buscar usuários por role: " + e.getMessage()));
        }
    }

    /**
     * ✏️ Atualizar usuário (ADMIN ou próprio usuário)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @Operation(summary = "Atualizar usuário", description = "Atualiza dados do usuário (ADMIN ou próprio usuário)")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser, Authentication auth) {
        try {
            User currentUser = (User) auth.getPrincipal();
            
            // Verificar se é o próprio usuário tentando alterar roles (não permitido)
            if (currentUser.getId().equals(id) && !currentUser.hasRole(Role.ADMIN)) {
                if (updatedUser.getRoles() != null && !updatedUser.getRoles().equals(currentUser.getRoles())) {
                    return ResponseEntity.badRequest()
                        .body(MessageResponse.error("Usuários não podem alterar suas próprias roles"));
                }
            }

            User user = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(MessageResponse.success(
                "Usuário atualizado com sucesso",
                user
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Erro ao atualizar usuário: " + e.getMessage()));
        }
    }

    /**
     * 🔒 Alterar senha (próprio usuário ou ADMIN)
     */
    @PutMapping("/{id}/change-password")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @Operation(summary = "Alterar senha", description = "Altera senha do usuário (próprio usuário ou ADMIN)")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest request) {
        try {
            userService.changePassword(id, request.getNewPassword());
            return ResponseEntity.ok(MessageResponse.success("Senha alterada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Erro ao alterar senha: " + e.getMessage()));
        }
    }

    /**
     * 🔓 Habilitar usuário (apenas ADMIN)
     */
    @PutMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Habilitar usuário", description = "Habilita usuário (apenas ADMIN)")
    public ResponseEntity<?> enableUser(@PathVariable Long id) {
        try {
            userService.enableUser(id);
            return ResponseEntity.ok(MessageResponse.success("Usuário habilitado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Erro ao habilitar usuário: " + e.getMessage()));
        }
    }

    /**
     * 🔒 Desabilitar usuário (apenas ADMIN)
     */
    @PutMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desabilitar usuário", description = "Desabilita usuário (apenas ADMIN)")
    public ResponseEntity<?> disableUser(@PathVariable Long id) {
        try {
            userService.disableUser(id);
            return ResponseEntity.ok(MessageResponse.success("Usuário desabilitado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Erro ao desabilitar usuário: " + e.getMessage()));
        }
    }

    /**
     * 🗑️ Deletar usuário (apenas ADMIN)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar usuário", description = "Remove usuário do sistema (apenas ADMIN)")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Authentication auth) {
        try {
            User currentUser = (User) auth.getPrincipal();
            
            // Impedir que admin delete a si mesmo
            if (currentUser.getId().equals(id)) {
                return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Você não pode deletar sua própria conta"));
            }

            userService.deleteUser(id);
            return ResponseEntity.ok(MessageResponse.success("Usuário deletado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Erro ao deletar usuário: " + e.getMessage()));
        }
    }

    /**
     * 🔍 Buscar usuários por nome (ADMIN ou MANAGER)
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Buscar por nome", description = "Busca usuários por nome (ADMIN ou MANAGER)")
    public ResponseEntity<?> searchUsersByName(@RequestParam String name) {
        try {
            List<User> users = userService.findByNameContaining(name);
            return ResponseEntity.ok(MessageResponse.success(
                "Resultado da busca por: " + name,
                users
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Erro na busca: " + e.getMessage()));
        }
    }

    /**
     * 📊 Estatísticas de usuários (apenas ADMIN)
     */
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Estatísticas", description = "Estatísticas dos usuários (apenas ADMIN)")
    public ResponseEntity<?> getUserStats() {
        try {
            UserStats stats = new UserStats();
            stats.totalUsers = userService.findAll().size();
            stats.activeUsers = userService.countActiveUsers();
            stats.adminUsers = userService.countByRole(Role.ADMIN);
            stats.managerUsers = userService.countByRole(Role.MANAGER);
            stats.regularUsers = userService.countByRole(Role.USER);

            return ResponseEntity.ok(MessageResponse.success(
                "Estatísticas dos usuários",
                stats
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Erro ao gerar estatísticas: " + e.getMessage()));
        }
    }

    /**
     * 👤 Meu perfil (usuário autenticado)
     */
    @GetMapping("/me")
    @Operation(summary = "Meu perfil", description = "Retorna dados do usuário autenticado")
    public ResponseEntity<?> getMyProfile(Authentication auth) {
        try {
            User user = (User) auth.getPrincipal();
            return ResponseEntity.ok(MessageResponse.success(
                "Perfil do usuário",
                user
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Erro ao obter perfil: " + e.getMessage()));
        }
    }

    // 📋 Classes auxiliares
    public static class ChangePasswordRequest {
        private String newPassword;

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

    public static class UserStats {
        public long totalUsers;
        public long activeUsers;
        public long adminUsers;
        public long managerUsers;
        public long regularUsers;
    }
}
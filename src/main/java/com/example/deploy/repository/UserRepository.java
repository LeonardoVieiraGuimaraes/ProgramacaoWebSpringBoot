package com.example.deploy.repository;

import com.example.deploy.model.Role;
import com.example.deploy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 🗃️ Repository para User - Operações de banco de dados
 * 
 * Contém métodos customizados para buscar usuários por diferentes critérios
 * e operações específicas de segurança
 * 
 * @author Leonardo Vieira Guimarães
 * @since 2025-09-23
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 🔍 Buscar usuário por username (usado pelo Spring Security)
     */
    Optional<User> findByUsername(String username);

    /**
     * 📧 Buscar usuário por email
     */
    Optional<User> findByEmail(String email);

    /**
     * 🔍 Buscar usuário por username ou email
     */
    Optional<User> findByUsernameOrEmail(String username, String email);

    /**
     * ✅ Verificar se username já existe
     */
    boolean existsByUsername(String username);

    /**
     * ✅ Verificar se email já existe
     */
    boolean existsByEmail(String email);

    /**
     * 🎭 Buscar usuários por role
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    List<User> findByRole(@Param("role") Role role);

    /**
     * 👥 Buscar usuários ativos
     */
    List<User> findByEnabledTrue();

    /**
     * 👥 Buscar usuários inativos
     */
    List<User> findByEnabledFalse();

    /**
     * 📅 Buscar usuários criados após uma data
     */
    List<User> findByCreatedAtAfter(LocalDateTime date);

    /**
     * 🔍 Buscar usuários por nome (busca parcial - firstName ou lastName)
     */
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByNameContaining(@Param("name") String name);

    /**
     * 🔐 Atualizar último login do usuário
     */
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = :loginTime WHERE u.id = :userId")
    void updateLastLogin(@Param("userId") Long userId, @Param("loginTime") LocalDateTime loginTime);

    /**
     * 🔒 Desabilitar usuário
     */
    @Modifying
    @Query("UPDATE User u SET u.enabled = false WHERE u.id = :userId")
    void disableUser(@Param("userId") Long userId);

    /**
     * 🔓 Habilitar usuário
     */
    @Modifying
    @Query("UPDATE User u SET u.enabled = true WHERE u.id = :userId")
    void enableUser(@Param("userId") Long userId);

    /**
     * 📊 Contar usuários por role
     */
    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r = :role")
    long countByRole(@Param("role") Role role);

    /**
     * 📊 Contar usuários ativos
     */
    long countByEnabledTrue();

    /**
     * 🔍 Buscar usuários com paginação e ordenação por nome
     */
    @Query("SELECT u FROM User u ORDER BY u.firstName, u.lastName")
    List<User> findAllOrderByName();
}
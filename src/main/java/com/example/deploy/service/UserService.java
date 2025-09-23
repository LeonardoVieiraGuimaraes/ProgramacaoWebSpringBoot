package com.example.deploy.service;

import com.example.deploy.dto.SignupRequest;
import com.example.deploy.model.Role;
import com.example.deploy.model.User;
import com.example.deploy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 👤 UserService - Serviço de gerenciamento de usuários
 * 
 * Responsável por operações CRUD de usuários e funcionalidades relacionadas
 * 
 * @author Leonardo Vieira Guimarães
 * @since 2025-09-23
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 📝 Criar novo usuário
     */
    public User createUser(SignupRequest signupRequest) {
        // ✅ Verificar se username já existe
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new RuntimeException("Erro: Username já está em uso!");
        }

        // ✅ Verificar se email já existe
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Erro: Email já está em uso!");
        }

        // 🔐 Criar usuário
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setRoles(signupRequest.getRoles());

        return userRepository.save(user);
    }

    /**
     * 📝 Criar usuário com roles padrão
     */
    public User createUserWithDefaultRole(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Set.of(Role.USER)); // Role padrão

        return userRepository.save(user);
    }

    /**
     * 🔍 Buscar usuário por ID
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 🔍 Buscar usuário por username
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 🔍 Buscar usuário por email
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * 👥 Listar todos os usuários
     */
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * 👥 Listar usuários ativos
     */
    @Transactional(readOnly = true)
    public List<User> findActiveUsers() {
        return userRepository.findByEnabledTrue();
    }

    /**
     * 🎭 Listar usuários por role
     */
    @Transactional(readOnly = true)
    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    /**
     * ✏️ Atualizar usuário
     */
    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        // Atualizar campos permitidos
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new RuntimeException("Email já está em uso!");
            }
            user.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getFirstName() != null) {
            user.setFirstName(updatedUser.getFirstName());
        }

        if (updatedUser.getLastName() != null) {
            user.setLastName(updatedUser.getLastName());
        }

        if (updatedUser.getRoles() != null) {
            user.setRoles(updatedUser.getRoles());
        }

        return userRepository.save(user);
    }

    /**
     * 🔒 Alterar senha do usuário
     */
    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + userId));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * 🔓 Habilitar usuário
     */
    public void enableUser(Long userId) {
        userRepository.enableUser(userId);
    }

    /**
     * 🔒 Desabilitar usuário
     */
    public void disableUser(Long userId) {
        userRepository.disableUser(userId);
    }

    /**
     * 🔐 Atualizar último login
     */
    public void updateLastLogin(Long userId) {
        userRepository.updateLastLogin(userId, LocalDateTime.now());
    }

    /**
     * 🗑️ Deletar usuário
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * ✅ Verificar se username existe
     */
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * ✅ Verificar se email existe
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 📊 Contar usuários por role
     */
    @Transactional(readOnly = true)
    public long countByRole(Role role) {
        return userRepository.countByRole(role);
    }

    /**
     * 📊 Contar usuários ativos
     */
    @Transactional(readOnly = true)
    public long countActiveUsers() {
        return userRepository.countByEnabledTrue();
    }

    /**
     * 🔍 Buscar usuários por nome
     */
    @Transactional(readOnly = true)
    public List<User> findByNameContaining(String name) {
        return userRepository.findByNameContaining(name);
    }

    /**
     * 📅 Buscar usuários criados após uma data
     */
    @Transactional(readOnly = true)
    public List<User> findCreatedAfter(LocalDateTime date) {
        return userRepository.findByCreatedAtAfter(date);
    }
}
package com.example.deploy.service;

import com.example.deploy.model.User;
import com.example.deploy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 👤 Custom UserDetailsService - Serviço de carregamento de usuários
 * 
 * Implementa UserDetailsService do Spring Security para carregar
 * usuários do banco de dados durante o processo de autenticação
 * 
 * @author Leonardo Vieira Guimarães
 * @since 2025-09-23
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 🔍 Carregar usuário por username (usado pelo Spring Security)
     * 
     * @param username Username ou email do usuário
     * @return UserDetails do usuário encontrado
     * @throws UsernameNotFoundException se usuário não for encontrado
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 🔍 Buscar por username ou email
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException(
                    "Usuário não encontrado com username/email: " + username
                ));

        // ✅ Verificar se usuário está ativo
        if (!user.isEnabled()) {
            throw new UsernameNotFoundException(
                "Usuário desabilitado: " + username
            );
        }

        // 🔒 Verificar se conta não está bloqueada
        if (!user.isAccountNonLocked()) {
            throw new UsernameNotFoundException(
                "Conta bloqueada: " + username
            );
        }

        // ⏰ Verificar se conta não expirou
        if (!user.isAccountNonExpired()) {
            throw new UsernameNotFoundException(
                "Conta expirada: " + username
            );
        }

        // 🔑 Verificar se credenciais não expiraram
        if (!user.isCredentialsNonExpired()) {
            throw new UsernameNotFoundException(
                "Credenciais expiradas: " + username
            );
        }

        return user;
    }

    /**
     * 🔍 Carregar usuário por ID (método utilitário)
     * 
     * @param userId ID do usuário
     * @return UserDetails do usuário encontrado
     * @throws UsernameNotFoundException se usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(
                    "Usuário não encontrado com ID: " + userId
                ));

        return user;
    }
}
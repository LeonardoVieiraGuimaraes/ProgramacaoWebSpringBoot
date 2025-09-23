package com.example.deploy.model;

/**
 * 🎭 Enum Role - Define os papéis de usuário no sistema
 * 
 * ADMIN: Acesso total ao sistema
 * USER: Acesso básico do usuário
 * MANAGER: Acesso intermediário com permissões de gestão
 * 
 * @author Leonardo Vieira Guimarães
 * @since 2025-09-23
 */
public enum Role {
    /**
     * 👑 Administrador - Acesso total
     * Pode fazer qualquer operação no sistema
     */
    ADMIN("Administrador", "Acesso total ao sistema"),
    
    /**
     * 👤 Usuário - Acesso básico
     * Pode visualizar e gerenciar seus próprios dados
     */
    USER("Usuário", "Acesso básico do usuário"),
    
    /**
     * 👨‍💼 Gerente - Acesso intermediário
     * Pode gerenciar outros usuários e recursos
     */
    MANAGER("Gerente", "Acesso intermediário com permissões de gestão");

    private final String displayName;
    private final String description;

    Role(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Retorna o nome de exibição da role
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Retorna a descrição da role
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retorna a authority name para Spring Security
     */
    public String getAuthority() {
        return "ROLE_" + this.name();
    }

    @Override
    public String toString() {
        return displayName;
    }
}
-- 🔐 Script de Inicialização de Usuários - Sistema de Autenticação
-- Autor: Leonardo Vieira Guimarães
-- Data: 23 de setembro de 2025
-- Objetivo: Criar tabelas e inserir usuários padrão para teste

-- =====================================================
-- 📋 1. CRIAÇÃO DAS TABELAS (se não existirem)
-- =====================================================

-- 👤 Tabela de usuários
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    account_non_expired BOOLEAN DEFAULT TRUE,
    account_non_locked BOOLEAN DEFAULT TRUE,
    credentials_non_expired BOOLEAN DEFAULT TRUE,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
);

-- 🎭 Tabela de roles dos usuários
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =====================================================
-- 🗑️ 2. LIMPEZA DOS DADOS EXISTENTES (OPCIONAL)
-- =====================================================

-- Descomente as linhas abaixo se quiser limpar dados existentes
-- DELETE FROM user_roles;
-- DELETE FROM users;
-- ALTER TABLE users AUTO_INCREMENT = 1;

-- =====================================================
-- 👤 3. INSERÇÃO DE USUÁRIOS PADRÃO
-- =====================================================

-- 🔐 Senhas criptografadas com BCrypt (strength 12)
-- Senha original para todos: "123456"
-- Hash BCrypt: $2a$12$YQiQxpyI07VaLGMaO2U.Kec2Cy8.JLK8jUt5J1P.QFnOZKZhz6/j6

-- 👑 ADMIN - Acesso total
INSERT INTO users (username, email, password, first_name, last_name, enabled) VALUES 
('admin', 'admin@exemplo.com', '$2a$12$YQiQxpyI07VaLGMaO2U.Kec2Cy8.JLK8jUt5J1P.QFnOZKZhz6/j6', 'Admin', 'Sistema', TRUE);

-- 👨‍💼 MANAGER - Acesso gerencial
INSERT INTO users (username, email, password, first_name, last_name, enabled) VALUES 
('manager', 'manager@exemplo.com', '$2a$12$YQiQxpyI07VaLGMaO2U.Kec2Cy8.JLK8jUt5J1P.QFnOZKZhz6/j6', 'João', 'Gerente', TRUE);

-- 👤 USER - Acesso básico
INSERT INTO users (username, email, password, first_name, last_name, enabled) VALUES 
('user', 'user@exemplo.com', '$2a$12$YQiQxpyI07VaLGMaO2U.Kec2Cy8.JLK8jUt5J1P.QFnOZKZhz6/j6', 'Maria', 'Usuario', TRUE);

-- 👤 USER2 - Outro usuário básico
INSERT INTO users (username, email, password, first_name, last_name, enabled) VALUES 
('leonardo', 'leonardo@exemplo.com', '$2a$12$YQiQxpyI07VaLGMaO2U.Kec2Cy8.JLK8jUt5J1P.QFnOZKZhz6/j6', 'Leonardo', 'Guimarães', TRUE);

-- 🔒 USER DESABILITADO - Para teste
INSERT INTO users (username, email, password, first_name, last_name, enabled) VALUES 
('disabled', 'disabled@exemplo.com', '$2a$12$YQiQxpyI07VaLGMaO2U.Kec2Cy8.JLK8jUt5J1P.QFnOZKZhz6/j6', 'Usuario', 'Desabilitado', FALSE);

-- =====================================================
-- 🎭 4. ATRIBUIÇÃO DE ROLES
-- =====================================================

-- 👑 Admin com role ADMIN
INSERT INTO user_roles (user_id, role) VALUES 
((SELECT id FROM users WHERE username = 'admin'), 'ADMIN');

-- 👨‍💼 Manager com roles MANAGER e USER
INSERT INTO user_roles (user_id, role) VALUES 
((SELECT id FROM users WHERE username = 'manager'), 'MANAGER'),
((SELECT id FROM users WHERE username = 'manager'), 'USER');

-- 👤 Users com role USER
INSERT INTO user_roles (user_id, role) VALUES 
((SELECT id FROM users WHERE username = 'user'), 'USER'),
((SELECT id FROM users WHERE username = 'leonardo'), 'USER'),
((SELECT id FROM users WHERE username = 'disabled'), 'USER');

-- =====================================================
-- 📊 5. VERIFICAÇÃO DOS DADOS INSERIDOS
-- =====================================================

-- Listar todos os usuários
SELECT 
    u.id,
    u.username,
    u.email,
    u.first_name,
    u.last_name,
    u.enabled,
    u.created_at,
    GROUP_CONCAT(ur.role) as roles
FROM users u
LEFT JOIN user_roles ur ON u.id = ur.user_id
GROUP BY u.id, u.username, u.email, u.first_name, u.last_name, u.enabled, u.created_at
ORDER BY u.id;

-- =====================================================
-- 🧪 6. DADOS PARA TESTE
-- =====================================================

/*
USUÁRIOS CRIADOS PARA TESTE:

1. ADMIN (Administrador)
   - Username: admin
   - Email: admin@exemplo.com
   - Senha: 123456
   - Roles: ADMIN
   - Acesso: Total

2. MANAGER (Gerente)
   - Username: manager
   - Email: manager@exemplo.com
   - Senha: 123456
   - Roles: MANAGER, USER
   - Acesso: Gerencial + Usuário

3. USER (Usuário básico)
   - Username: user
   - Email: user@exemplo.com
   - Senha: 123456
   - Roles: USER
   - Acesso: Básico

4. LEONARDO (Usuário desenvolvedor)
   - Username: leonardo
   - Email: leonardo@exemplo.com
   - Senha: 123456
   - Roles: USER
   - Acesso: Básico

5. DISABLED (Usuário desabilitado)
   - Username: disabled
   - Email: disabled@exemplo.com
   - Senha: 123456
   - Roles: USER
   - Status: DESABILITADO
   - Acesso: Negado

ENDPOINTS PARA TESTE:

1. Login:
   POST /auth/login
   Body: {"username": "admin", "password": "123456"}

2. Registro:
   POST /auth/signup
   Body: {
     "username": "novousuario",
     "email": "novo@exemplo.com",
     "password": "123456",
     "firstName": "Novo",
     "lastName": "Usuario",
     "roles": ["USER"]
   }

3. Refresh Token:
   POST /auth/refresh
   Header: Authorization: Bearer {seu_token}

4. Verificar Token:
   GET /auth/verify
   Header: Authorization: Bearer {seu_token}

5. Perfil:
   GET /auth/profile
   Header: Authorization: Bearer {seu_token}
*/

-- =====================================================
-- 🔍 7. CONSULTAS ÚTEIS PARA DEBUG
-- =====================================================

-- Contar usuários por role
SELECT role, COUNT(*) as total 
FROM user_roles 
GROUP BY role;

-- Usuários ativos vs inativos
SELECT 
    enabled,
    COUNT(*) as total
FROM users 
GROUP BY enabled;

-- Último login dos usuários
SELECT 
    username,
    email,
    last_login,
    CASE 
        WHEN last_login IS NULL THEN 'Nunca fez login'
        WHEN last_login > DATE_SUB(NOW(), INTERVAL 1 DAY) THEN 'Ativo hoje'
        WHEN last_login > DATE_SUB(NOW(), INTERVAL 7 DAY) THEN 'Ativo esta semana'
        ELSE 'Inativo há mais de 7 dias'
    END as status_login
FROM users
ORDER BY last_login DESC;

-- =====================================================
-- ✅ SCRIPT CONCLUÍDO
-- =====================================================
-- Execute este script para criar a estrutura completa
-- de autenticação com usuários de teste
-- 
-- ⚠️ IMPORTANTE: 
-- - Altere as senhas em produção
-- - Use senhas mais seguras
-- - Configure HTTPS em produção
-- - Revise as roles conforme sua necessidade
-- =====================================================
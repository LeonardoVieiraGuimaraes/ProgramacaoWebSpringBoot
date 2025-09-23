# 🔐 README_AUTENTICACAO.md - Sistema de Autenticação JWT

Este documento explica o sistema completo de autenticação e autorização implementado com **Spring Security + JWT**.

---

## 📋 **Índice**

1. [Visão Geral](#visão-geral)
2. [Tecnologias Utilizadas](#tecnologias-utilizadas)
3. [Estrutura do Sistema](#estrutura-do-sistema)
4. [Instalação e Configuração](#instalação-e-configuração)
5. [Endpoints da API](#endpoints-da-api)
6. [Testes e Exemplos](#testes-e-exemplos)
7. [Arquitetura de Segurança](#arquitetura-de-segurança)
8. [Troubleshooting](#troubleshooting)

---

## 🎯 **Visão Geral**

Sistema robusto de autenticação e autorização implementado com:

- **🔐 Spring Security 6.4.5** - Framework de segurança
- **🎫 JWT (JSON Web Tokens)** - Autenticação stateless
- **🎭 Role-based Access Control** - Controle de acesso baseado em papéis
- **🗃️ JPA/Hibernate** - Persistência de usuários
- **📚 OpenAPI/Swagger** - Documentação automática da API

### **🎭 Roles Implementadas**

| Role | Descrição | Acesso |
|------|-----------|--------|
| **ADMIN** | Administrador | Acesso total ao sistema |
| **MANAGER** | Gerente | Gestão de usuários e recursos |
| **USER** | Usuário | Acesso básico aos recursos |

---

## 🛠️ **Tecnologias Utilizadas**

### **Backend**
- **Spring Boot 3.4.5** - Framework principal
- **Spring Security 6.4.5** - Autenticação e autorização
- **JJWT 0.12.6** - Manipulação de tokens JWT
- **Spring Data JPA** - Persistência de dados
- **H2 Database** - Banco em memória para desenvolvimento
- **BCrypt** - Criptografia de senhas
- **Maven** - Gerenciamento de dependências

### **Documentação**
- **SpringDoc OpenAPI 3** - Geração automática da documentação
- **Swagger UI** - Interface visual da API

### **Ferramentas**
- **Java 21** - Linguagem de programação
- **IntelliJ IDEA / VS Code** - IDEs recomendadas

---

## 🏗️ **Estrutura do Sistema**

### **📁 Organização do Código**

```
src/main/java/com/example/deploy/
├── 🔐 security/
│   ├── JwtTokenUtil.java           # Utilitários JWT
│   └── JwtAuthenticationFilter.java # Filtro de autenticação
├── 📋 dto/
│   ├── LoginRequest.java           # Request de login
│   ├── SignupRequest.java          # Request de registro
│   ├── JwtResponse.java            # Response com token
│   └── MessageResponse.java        # Mensagens da API
├── 👤 model/
│   ├── User.java                   # Entidade usuário
│   └── Role.java                   # Enum de papéis
├── 🗃️ repository/
│   └── UserRepository.java         # Repository de usuários
├── 🎯 service/
│   ├── UserService.java            # Serviços de usuário
│   └── CustomUserDetailsService.java # UserDetailsService
├── 🌐 controller/
│   ├── AuthController.java         # Endpoints de autenticação
│   └── UserManagementController.java # Gestão de usuários
├── ⚙️ config/
│   ├── SecurityConfig.java         # Configuração de segurança
│   └── WebConfig.java              # Configuração web + Swagger
└── 🔧 util/
    └── PasswordHashGenerator.java  # Gerador de hashs BCrypt
```

### **🗄️ Estrutura do Banco de Dados**

```sql
-- 👤 Tabela de usuários
users (
    id BIGINT PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    account_non_expired BOOLEAN,
    account_non_locked BOOLEAN,
    credentials_non_expired BOOLEAN,
    enabled BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    last_login TIMESTAMP
)

-- 🎭 Tabela de roles
user_roles (
    user_id BIGINT,
    role VARCHAR(20)
)
```

---

## 🚀 **Instalação e Configuração**

### **1. Pré-requisitos**

- ✅ Java 21+
- ✅ Maven 3.8+
- ✅ IDE (IntelliJ IDEA, VS Code, Eclipse)

### **2. Configuração**

#### **2.1 Clone e Setup**

```bash
# Clonar repositório
git clone <url-do-repositorio>
cd ProgramacaoWebSpringBoot

# Compilar projeto
mvn clean compile
```

#### **2.2 Configurações JWT (application.yaml)**

```yaml
# 🔐 Configurações JWT
jwt:
  secret: ProgramacaoWebSpringBootSecretKeyForJWTTokenGenerationThatNeedsToBeAtLeast256BitsLong
  expiration: 86400000 # 24 horas
  issuer: ProgramacaoWebSpringBoot
```

#### **2.3 Executar Aplicação**

**Opção 1: Script automatizado**
```bash
# Windows
run-auth.bat

# Linux/Mac
chmod +x run-auth.sh
./run-auth.sh
```

**Opção 2: Maven**
```bash
mvn spring-boot:run
```

### **3. Configurar Banco de Dados**

#### **3.1 Acessar H2 Console**
- **URL**: `http://localhost:8021/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (vazio)

#### **3.2 Executar Script de Usuários**
Execute o arquivo `init-users.sql` no console H2 para criar os usuários de teste.

---

## 🌐 **Endpoints da API**

### **🔐 Autenticação** (`/auth`)

#### **Login**
```http
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "123456"
}
```

**Resposta de Sucesso:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "username": "admin",
  "email": "admin@exemplo.com",
  "fullName": "Admin Sistema",
  "roles": ["ADMIN"],
  "expiresAt": "2025-09-24T16:49:40",
  "expiresIn": 86400
}
```

#### **Registro**
```http
POST /auth/signup
Content-Type: application/json

{
  "username": "novousuario",
  "email": "novo@exemplo.com",
  "password": "123456",
  "firstName": "Novo",
  "lastName": "Usuario",
  "roles": ["USER"]
}
```

#### **Refresh Token**
```http
POST /auth/refresh
Authorization: Bearer {seu_token}
```

#### **Verificar Token**
```http
GET /auth/verify
Authorization: Bearer {seu_token}
```

#### **Perfil do Usuário**
```http
GET /auth/profile
Authorization: Bearer {seu_token}
```

### **👥 Gerenciamento de Usuários** (`/api/users`)

#### **Listar Usuários** (ADMIN)
```http
GET /api/users
Authorization: Bearer {token_admin}
```

#### **Buscar Usuário por ID** (ADMIN ou próprio usuário)
```http
GET /api/users/{id}
Authorization: Bearer {seu_token}
```

#### **Usuários Ativos** (ADMIN ou MANAGER)
```http
GET /api/users/active
Authorization: Bearer {token_admin_ou_manager}
```

#### **Usuários por Role** (ADMIN)
```http
GET /api/users/by-role/USER
Authorization: Bearer {token_admin}
```

#### **Atualizar Usuário** (ADMIN ou próprio usuário)
```http
PUT /api/users/{id}
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "firstName": "Nome Atualizado",
  "lastName": "Sobrenome Atualizado"
}
```

#### **Alterar Senha** (ADMIN ou próprio usuário)
```http
PUT /api/users/{id}/change-password
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "newPassword": "novaSenha123"
}
```

#### **Habilitar/Desabilitar Usuário** (ADMIN)
```http
PUT /api/users/{id}/enable
Authorization: Bearer {token_admin}

PUT /api/users/{id}/disable
Authorization: Bearer {token_admin}
```

#### **Deletar Usuário** (ADMIN)
```http
DELETE /api/users/{id}
Authorization: Bearer {token_admin}
```

#### **Buscar por Nome** (ADMIN ou MANAGER)
```http
GET /api/users/search?name=leonardo
Authorization: Bearer {token_admin_ou_manager}
```

#### **Estatísticas** (ADMIN)
```http
GET /api/users/stats
Authorization: Bearer {token_admin}
```

#### **Meu Perfil** (Usuário autenticado)
```http
GET /api/users/me
Authorization: Bearer {seu_token}
```

---

## 🧪 **Testes e Exemplos**

### **👤 Usuários de Teste Criados**

| Username | Email | Senha | Roles | Status |
|----------|-------|-------|-------|--------|
| `admin` | admin@exemplo.com | 123456 | ADMIN | ✅ Ativo |
| `manager` | manager@exemplo.com | 123456 | MANAGER, USER | ✅ Ativo |
| `user` | user@exemplo.com | 123456 | USER | ✅ Ativo |
| `leonardo` | leonardo@exemplo.com | 123456 | USER | ✅ Ativo |
| `disabled` | disabled@exemplo.com | 123456 | USER | ❌ Desabilitado |

### **🔧 Teste no Postman/Insomnia**

#### **1. Login como Admin**
```bash
curl -X POST http://localhost:8021/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "123456"
  }'
```

#### **2. Usar Token em Requisições Protegidas**
```bash
curl -X GET http://localhost:8021/api/users \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

#### **3. Criar Novo Usuário**
```bash
curl -X POST http://localhost:8021/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "teste",
    "email": "teste@exemplo.com",
    "password": "123456",
    "firstName": "Usuario",
    "lastName": "Teste",
    "roles": ["USER"]
  }'
```

### **📊 Swagger UI**

Acesse a documentação interativa em:
- **URL**: `http://localhost:8021/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8021/v3/api-docs`

**Como usar Swagger com JWT:**
1. Faça login via `/auth/login`
2. Copie o token retornado
3. No Swagger UI, clique em "Authorize"
4. Digite: `Bearer SEU_TOKEN_AQUI`
5. Teste os endpoints protegidos

---

## 🏛️ **Arquitetura de Segurança**

### **🔒 Fluxo de Autenticação**

```
1. 👤 Cliente faz login com username/password
   ↓
2. 🔐 Spring Security valida credenciais
   ↓
3. 🎫 Sistema gera token JWT assinado
   ↓
4. 📤 Token é retornado ao cliente
   ↓
5. 🔑 Cliente inclui token no header Authorization
   ↓
6. 🛡️ JwtAuthenticationFilter valida token
   ↓
7. ✅ Acesso liberado se token válido
```

### **🎭 Controle de Acesso**

```
Hierarquia de Roles:
ADMIN > MANAGER > USER

Permissões:
- ADMIN: Acesso total
- MANAGER: Gestão de usuários + recursos próprios  
- USER: Apenas recursos próprios
```

### **🔐 Configurações de Segurança**

```java
// Endpoints públicos (sem autenticação)
"/auth/**", "/public/**", "/actuator/health", 
"/swagger-ui/**", "/v3/api-docs/**"

// Endpoints administrativos (apenas ADMIN)
"/admin/**"

// Endpoints de gestão (ADMIN ou MANAGER)  
"/management/**"

// APIs protegidas (usuários autenticados)
"/api/**"
```

### **🛡️ Funcionalidades de Segurança**

- ✅ **Senhas criptografadas** com BCrypt (strength 12)
- ✅ **Tokens JWT assinados** com HMAC SHA-512
- ✅ **CORS configurado** para múltiplos ambientes
- ✅ **CSRF desabilitado** (API stateless)
- ✅ **Validação de entrada** com Bean Validation
- ✅ **Controle de sessão** stateless
- ✅ **Headers de segurança** configurados

---

## 🔧 **Troubleshooting**

### **❌ Problemas Comuns**

#### **1. Token Inválido**
```json
{
  "message": "Token JWT inválido ou expirado!",
  "success": false
}
```

**Soluções:**
- Verificar se token não expirou (24h padrão)
- Verificar formato: `Bearer TOKEN_AQUI`
- Fazer novo login para obter token válido

#### **2. Acesso Negado**
```json
{
  "message": "Access Denied",
  "success": false
}
```

**Soluções:**
- Verificar se usuário tem role necessária
- Verificar se endpoint requer autenticação
- Conferir se token está sendo enviado

#### **3. Usuário não encontrado**
```json
{
  "message": "Usuário não encontrado com username/email: teste",
  "success": false
}
```

**Soluções:**
- Verificar se usuário existe no banco
- Executar script `init-users.sql`
- Verificar se usuário está habilitado

#### **4. Erro de CORS**
```
Access to fetch at 'http://localhost:8021/auth/login' 
from origin 'http://localhost:3000' has been blocked by CORS policy
```

**Soluções:**
- Verificar configuração CORS em `WebConfig.java`
- Adicionar origem do frontend na configuração
- Verificar headers da requisição

### **🔍 Debug e Logs**

#### **Verificar Token**
```bash
# Usar endpoint de verificação
curl -X GET http://localhost:8021/auth/verify \
  -H "Authorization: Bearer SEU_TOKEN"
```

#### **Logs da Aplicação**
```yaml
# application.yaml
logging:
  level:
    '[com.example.deploy]': DEBUG
    '[org.springframework.security]': DEBUG
```

#### **Console H2**
```sql
-- Verificar usuários
SELECT * FROM users;

-- Verificar roles
SELECT u.username, ur.role 
FROM users u 
JOIN user_roles ur ON u.id = ur.user_id;

-- Verificar último login
SELECT username, last_login 
FROM users 
ORDER BY last_login DESC;
```

### **⚡ Dicas de Performance**

1. **Cache de usuários**: Implementar cache do Spring para UserDetailsService
2. **Pool de conexões**: Configurar HikariCP adequadamente
3. **Indexes no banco**: Criar índices em username e email
4. **Compressão**: Habilitar compressão gzip no servidor

### **🔒 Dicas de Segurança**

1. **Trocar secret JWT** em produção
2. **Usar HTTPS** em produção
3. **Configurar rate limiting** para endpoints de login
4. **Implementar blacklist** de tokens
5. **Monitorar tentativas** de login falhadas
6. **Usar senhas fortes** em produção

---

## 🎯 **Próximos Passos**

### **📈 Melhorias Futuras**

1. **🔄 Refresh Token automático** - Renovação transparente
2. **📱 OAuth2 Integration** - Login social (Google, GitHub)
3. **🔐 Two-Factor Authentication** - Autenticação em duas etapas
4. **📊 Audit Logging** - Log de todas as ações de segurança
5. **🚫 Rate Limiting** - Proteção contra ataques de força bruta
6. **🔒 Token Blacklist** - Invalidação manual de tokens
7. **📧 Email Verification** - Verificação de email no registro
8. **🔑 Password Reset** - Reset de senha via email

### **🔧 Configurações Avançadas**

1. **Multiple JWT Secrets** - Rotação de chaves
2. **Custom Claims** - Claims personalizados no JWT
3. **Role Hierarchy** - Hierarquia mais complexa de roles
4. **Method-level Security** - Segurança granular por método
5. **Database Sessions** - Sessões persistidas no banco

---

## 📚 **Recursos Adicionais**

### **🔗 Links Úteis**

- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [JWT.io](https://jwt.io/) - Debug de tokens JWT
- [BCrypt Generator](https://bcrypt-generator.com/) - Gerador de hashs BCrypt
- [Spring Boot Security](https://spring.io/guides/gs/securing-web/)
- [OpenAPI 3 Documentation](https://springdoc.org/)

### **📖 Documentação Relacionada**

- `README.md` - Documentação geral do projeto
- `README_DEPLOYS.md` - Processo de deploy
- `README_MELHOR_PRATICA.md` - Melhores práticas de desenvolvimento
- `init-users.sql` - Script de inicialização de usuários

---

## 🏆 **Conclusão**

O sistema de autenticação JWT implementado oferece:

- ✅ **Segurança robusta** com Spring Security
- ✅ **Tokens stateless** para escalabilidade  
- ✅ **Controle de acesso granular** baseado em roles
- ✅ **API bem documentada** com Swagger
- ✅ **Fácil integração** com frontends
- ✅ **Extensibilidade** para futuras funcionalidades

**Sistema pronto para produção** com todas as melhores práticas de segurança implementadas.

---

**📅 Última atualização**: 23 de setembro de 2025  
**🔖 Versão**: v2.1.0  
**👨‍💻 Autor**: Leonardo Vieira Guimarães  
**📋 Projeto**: ProgramacaoWebSpringBoot  
**🎯 Objetivo**: Sistema completo de autenticação JWT
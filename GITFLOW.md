# 🌊 GitFlow - Estratégia de Branching

## 📋 Estrutura de Branches

### 🌿 **Branches Principais:**

#### 1. **`main`** (Produção)
- **Ambiente:** Produção
- **URL:** https://prowebv01.leoproti.com.br:8013
- **Estabilidade:** Sempre estável
- **Deploy:** Automático via tag
- **Acesso:** Apenas merge de `staging`

#### 2. **`staging`** (Homologação) 
- **Ambiente:** Homologação/QA
- **URL:** https://staging.prowebv01.leoproti.com.br:8014
- **Estabilidade:** Estável para testes
- **Deploy:** Automático via push
- **Acesso:** Merge de `develop` + features prontas

#### 3. **`develop`** (Desenvolvimento)
- **Ambiente:** Desenvolvimento
- **URL:** https://dev.prowebv01.leoproti.com.br:8015
- **Estabilidade:** Instável (desenvolvimento ativo)
- **Deploy:** Automático via push
- **Acesso:** Features integradas

### 🚀 **Branches Temporárias:**

#### **`feature/*`** (Funcionalidades)
```bash
feature/nova-api-produtos
feature/login-oauth
feature/dashboard-admin
```

#### **`hotfix/*`** (Correções Urgentes)
```bash
hotfix/bug-critico-login
hotfix/security-patch
```

#### **`release/*`** (Preparação de Release)
```bash
release/v1.2.0
release/v1.3.0
```

## 🎯 **Fluxo de Trabalho**

### 1. **Desenvolvimento de Feature:**
```bash
# Criar feature branch
git checkout develop
git pull origin develop
git checkout -b feature/nova-funcionalidade

# Desenvolver
# ... código ...

# Finalizar feature
git checkout develop
git merge feature/nova-funcionalidade
git push origin develop
# → Deploy automático em DEV
```

### 2. **Preparar Homologação:**
```bash
# Quando develop estiver estável
git checkout staging
git merge develop
git push origin staging
# → Deploy automático em STAGING
```

### 3. **Release para Produção:**
```bash
# Após testes em staging
git checkout main
git merge staging
git tag v1.2.0
git push origin main --tags
# → Deploy automático em PRODUÇÃO
```

### 4. **Hotfix Crítico:**
```bash
# Bug crítico em produção
git checkout main
git checkout -b hotfix/bug-critico
# ... correção ...
git checkout main
git merge hotfix/bug-critico
git checkout staging
git merge hotfix/bug-critico  
git checkout develop
git merge hotfix/bug-critico
git tag v1.2.1
git push origin --all --tags
# → Deploy em todos os ambientes
```

## 🏷️ **Versionamento Semântico**

### Formato: `MAJOR.MINOR.PATCH`

- **MAJOR** (1.0.0 → 2.0.0): Mudanças incompatíveis
- **MINOR** (1.0.0 → 1.1.0): Novas funcionalidades compatíveis  
- **PATCH** (1.0.0 → 1.0.1): Correções de bugs

### Exemplos:
```
v1.0.0 - Release inicial
v1.1.0 - Nova API de produtos
v1.1.1 - Correção bug CORS
v1.2.0 - Sistema de autenticação
v2.0.0 - Nova arquitetura (breaking change)
```

## 🎮 **Comandos Práticos**

### Setup Inicial:
```bash
# Criar branches base
git checkout -b develop
git push -u origin develop

git checkout -b staging  
git push -u origin staging
```

### Desenvolvimento:
```bash
# Nova feature
git checkout develop
git checkout -b feature/minha-feature
# ... desenvolvimento ...
git checkout develop
git merge feature/minha-feature
git push origin develop

# Promover para staging
git checkout staging
git merge develop
git push origin staging

# Release para produção
git checkout main
git merge staging
git tag v1.1.0
git push origin main --tags
```

## 📊 **Proteção de Branches**

### No GitHub, configurar:
- **`main`**: Protegida, apenas via PR de `staging`
- **`staging`**: Protegida, apenas via PR de `develop`
- **`develop`**: Aberta para features

## 🔍 **Monitoramento**

### URLs de Monitoramento:
```
DEV:     https://dev.prowebv01.leoproti.com.br:8015/actuator/health
STAGING: https://staging.prowebv01.leoproti.com.br:8014/actuator/health  
PROD:    https://prowebv01.leoproti.com.br:8013/actuator/health
```
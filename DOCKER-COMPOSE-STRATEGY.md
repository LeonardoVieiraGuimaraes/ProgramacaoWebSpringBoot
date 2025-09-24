# 📋 Estratégia de Docker Compose por Branch

Esta documentação explica como funcionam os arquivos Docker Compose específicos para cada ambiente e branch no projeto ProWeb.

## 🎯 Arquivos Docker Compose

### 📄 `docker-compose.yml` (Desenvolvimento/v01)
- **Branch**: `v01`, `main` (desenvolvimento)
- **Porta**: `8013`
- **Profile**: `dev`
- **Uso**: Ambiente de desenvolvimento local
- **Características**:
  - Console H2 habilitado para debug
  - Logs em nível DEBUG
  - Banco de dados isolado para dev
  - Perfil de desenvolvimento ativo

### 📄 `docker-compose-staging.yml` (Homologação)
- **Branch**: `staging`
- **Porta**: `8020`
- **Profile**: `staging`
- **Uso**: Ambiente de homologação/teste
- **Características**:
  - Console H2 desabilitado por segurança
  - Logs em nível INFO/DEBUG
  - Banco de dados isolado para staging
  - Configurações otimizadas para testes
  - Nginx proxy reverso opcional
  - Recursos limitados para simular produção

## 🚀 Workflow de Deploy (GitHub Actions)

O arquivo `.github/workflows/ProWebDeploy.yml` foi modificado para usar automaticamente o arquivo Docker Compose correto baseado no ambiente:

### 🔄 Lógica de Seleção
```yaml
# Para branch staging
if [ "$environment" == "staging" ]; then
  DOCKER_COMPOSE_FILE=docker-compose-staging.yml
else
  DOCKER_COMPOSE_FILE=docker-compose.yml
fi
```

### 📋 Variáveis de Ambiente por Branch

#### 🟢 Staging Branch
```yaml
environment: staging
project_name: proweb-staging
domain: staging.proweb.leoproti.com.br
port: 8020
spring_profile: staging
```

#### 🔵 Main Branch / Production
```yaml
environment: production
project_name: proweb-prod
domain: proweb.leoproti.com.br
port: 8021
spring_profile: prod
```

## 🔧 Como Funciona o Deploy

### 1️⃣ Detecção do Ambiente
O workflow automaticamente detecta qual ambiente usar baseado na branch:
- `staging` → usa `docker-compose-staging.yml`
- `main` ou tags → usa `docker-compose.yml` (com configurações de produção)

### 2️⃣ Transferência de Arquivos
```bash
# O workflow transfere o arquivo correto
rsync $DOCKER_COMPOSE_FILE user@server:/path/docker-compose.yml
```

### 3️⃣ Deploy no Servidor
```bash
# No servidor, sempre executa
docker-compose down --remove-orphans
docker-compose build --no-cache
docker-compose up -d
```

## 🌿 Estratégia de Branches e Merges

### 📝 Fluxo Recomendado
1. **Desenvolvimento** (`v01` branch):
   - Use `docker-compose.yml` padrão
   - Porta 8013 para desenvolvimento local
   - Profile `dev` com debugging habilitado

2. **Staging** (`staging` branch):
   - Merge de `v01` → `staging`
   - Use `docker-compose-staging.yml`
   - Porta 8020 para testes
   - Profile `staging` otimizado

3. **Produção** (`main` branch):
   - Merge de `staging` → `main`
   - Use `docker-compose.yml` (será sobescrito pelo workflow para produção)
   - Porta 8021 para produção
   - Profile `prod` otimizado

### ⚠️ Resolução de Conflitos

Se houver conflitos nos arquivos Docker Compose durante merges:

```bash
# Para manter configuração do staging
git merge v01 --strategy-option=ours -X ours docker-compose-staging.yml

# Para aplicar mudanças do v01 mas manter staging config
git checkout --ours docker-compose-staging.yml
git checkout --theirs outros-arquivos.java
```

## 🧪 Testando Localmente

### 🔵 Ambiente de Desenvolvimento
```bash
# Na branch v01
docker-compose up -d
# Aplicação rodará na porta 8013
```

### 🟡 Ambiente de Staging
```bash
# Na branch staging
docker-compose -f docker-compose-staging.yml up -d
# Aplicação rodará na porta 8020
```

### 🔴 Simulando Produção
```bash
# Criar arquivo docker-compose-prod.yml
docker-compose -f docker-compose-prod.yml up -d
# Aplicação rodará na porta 8021
```

## 📊 Monitoramento

### 🩺 Health Checks
Cada ambiente tem endpoints de monitoramento:

- **Dev**: http://localhost:8013/actuator/health
- **Staging**: https://staging.proweb.leoproti.com.br/actuator/health
- **Produção**: https://proweb.leoproti.com.br/actuator/health

### 📈 Recursos por Ambiente

| Ambiente | CPU | Memória | Porta | Profile | Console H2 |
|----------|-----|---------|-------|---------|------------|
| Dev      | -   | -       | 8013  | dev     | ✅         |
| Staging  | 1.0 | 1GB     | 8020  | staging | ❌         |
| Produção | -   | -       | 8021  | prod    | ❌         |

## 🎯 Benefícios desta Abordagem

1. **🔒 Isolamento**: Cada ambiente tem configurações próprias
2. **🚀 Simplicidade**: Sem conflitos de merge complexos
3. **⚡ Automatização**: Deploy automático escolhe o arquivo correto
4. **🔧 Flexibilidade**: Fácil de ajustar configurações por ambiente
5. **📊 Monitoramento**: Cada ambiente isolado para debugging
6. **🛡️ Segurança**: Staging/Prod sem console H2 exposto

## 📞 Comandos Úteis

```bash
# Verificar configuração do compose
docker-compose config

# Verificar configuração do staging
docker-compose -f docker-compose-staging.yml config

# Logs do ambiente atual
docker-compose logs -f

# Status dos containers
docker-compose ps

# Restart completo
docker-compose down --remove-orphans
docker-compose up -d --force-recreate
```
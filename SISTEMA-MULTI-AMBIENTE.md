# 🚀 Sistema Multi-Ambiente - ProWebV01

## 📋 Visão Geral

Este projeto implementa um sistema completo de **DevOps com 3 ambientes**:
- **Desenvolvimento** (DEV) - Testes e desenvolvimento
- **Homologação** (STAGING) - Testes finais antes de produção
- **Produção** (PROD) - Ambiente final do usuário

## 🏗️ Arquitetura de Ambientes

### 📊 Tabela de Configurações

| Ambiente | Branch | Porta | URL | Base de Dados |
|----------|--------|-------|-----|---------------|
| **Produção** | `v01` | `8013` | https://prowebv01.leoproti.com.br | `/data/h2db/proddb` |
| **Staging** | `main` | `8020` | https://staging.prowebv01.leoproti.com.br | `/data/h2db/staging-db` |
| **Desenvolvimento** | `feature/*` | `8021` | https://dev.prowebv01.leoproti.com.br | `/data/h2db/devdb` |

### 🔧 Configurações por Ambiente

#### 🟢 **Produção (v01)**
- **Branch:** `v01` (protegida)
- **Profile:** `prod`
- **Características:**
  - Logs em nível INFO
  - Console H2 desabilitado
  - Configurações de segurança máximas
  - Pool de conexões otimizado (20 conexões)

#### 🟡 **Staging/Homologação**
- **Branch:** `main`
- **Profile:** `staging`
- **Características:**
  - Logs em nível INFO
  - Console H2 desabilitado
  - Ambiente para testes finais
  - Pool de conexões médio (10 conexões)

#### 🔵 **Desenvolvimento**
- **Branch:** `feature/*` ou outras
- **Profile:** `dev`
- **Características:**
  - Logs em nível DEBUG
  - Console H2 habilitado
  - Configurações para desenvolvimento
  - Pool de conexões básico (5 conexões)

## 🚀 Fluxo de Deployment Automático

### 📁 Workflow: `.github/workflows/AdvancedDeploy.yml`

```yaml
# Detecta automaticamente o ambiente baseado na branch
# Aplica as configurações corretas
# Faz deploy com a porta e perfil corretos
```

### 🔄 Como Funciona

1. **Push** para qualquer branch
2. **GitHub Actions** detecta a branch automaticamente
3. **Determina o ambiente** baseado nas regras:
   - `v01` → Produção (porta 8013)
   - `main` → Staging (porta 8020)
   - Outras → Desenvolvimento (porta 8021)
4. **Faz o build** com o perfil correto
5. **Deploy** no servidor com as configurações do ambiente

## 📝 Como Usar o Sistema

### 🌟 **Para Desenvolvimento:**
```bash
# Crie uma nova feature
git checkout -b feature/nova-funcionalidade

# Desenvolva e teste
# Faça commit
git add .
git commit -m "Implementa nova funcionalidade"

# Push dispara deploy automático em DEV
git push origin feature/nova-funcionalidade
```

### 🎯 **Para Homologação:**
```bash
# Merge na main após code review
git checkout main
git merge feature/nova-funcionalidade

# Push dispara deploy automático em STAGING
git push origin main
```

### 🏆 **Para Produção:**
```bash
# Após testes em staging, crie release
git checkout v01
git merge main
git tag v01.1.0  # Versionamento semântico

# Push dispara deploy automático em PRODUÇÃO
git push origin v01 --tags
```

## 🔍 Monitoramento e Logs

### 📊 **Health Checks**
- **Produção:** https://prowebv01.leoproti.com.br/actuator/health
- **Staging:** https://staging.prowebv01.leoproti.com.br/actuator/health
- **Dev:** https://dev.prowebv01.leoproti.com.br/actuator/health

### 📝 **Localização dos Logs**
- **Produção:** `/var/log/prowebv01/prod-application.log`
- **Staging:** `/var/log/prowebv01/staging-application.log`
- **Dev:** `/var/log/prowebv01/dev-application.log`

### 🗄️ **Bases de Dados H2**
- **Produção:** `/data/h2db/proddb.mv.db`
- **Staging:** `/data/h2db/staging-db.mv.db`
- **Dev:** `/data/h2db/devdb.mv.db`

## 🛠️ Configurações Especiais

### 🌐 **CORS**
Configurado no `WebConfig.java` para aceitar requisições de:
- Todos os domínios `*.leoproti.com.br`
- Localhost para desenvolvimento

### 🐳 **Docker**
Cada ambiente roda em seu próprio container com:
- Portas isoladas
- Volumes de dados separados
- Configurações específicas

### 🔐 **Segurança**
- **Produção:** Máxima segurança, console H2 desabilitado
- **Staging:** Segurança alta, logs controlados
- **Dev:** Console H2 habilitado para debug

## 📚 **Comandos Úteis**

### 🔧 **Build Local**
```bash
# Build para produção
./mvnw clean package -Pprod

# Build para staging
./mvnw clean package -Pstaging

# Build para desenvolvimento
./mvnw clean package -Pdev
```

### 🐳 **Docker Local**
```bash
# Rodar produção localmente
docker-compose -f docker-compose.yml up

# Verificar logs
docker-compose logs -f app
```

### 📊 **Verificar Status**
```bash
# Verificar se aplicação está rodando
curl https://prowebv01.leoproti.com.br/actuator/health

# Verificar métricas
curl https://prowebv01.leoproti.com.br/actuator/metrics
```

## 🎯 **Endpoints Principais**

### 📚 **APIs RESTful**
- `GET /api/produtos` - Lista produtos
- `POST /api/produtos` - Cria produto
- `PUT /api/produtos/{id}` - Atualiza produto
- `DELETE /api/produtos/{id}` - Remove produto

- `GET /api/alunos` - Lista alunos
- `POST /api/alunos` - Cria aluno
- `PUT /api/alunos/{id}` - Atualiza aluno
- `DELETE /api/alunos/{id}` - Remove aluno

### 🖼️ **Views Web**
- `/produtos` - Gestão de produtos
- `/alunos` - Gestão de alunos
- `/hello` - Página de teste

## 🔄 **Estratégia GitFlow**

```
v01 (Produção)    ←─── merge após homologação
 ↑
main (Staging)    ←─── merge após dev
 ↑
feature/xyz (Dev) ←─── desenvolvimento ativo
```

## ⚡ **Quick Start**

1. **Clone o projeto**
2. **Crie uma branch feature**
3. **Desenvolva e teste**
4. **Push dispara deploy automático**
5. **Merge para main para homologação**
6. **Merge para v01 para produção**

---

## 🎉 **Benefícios desta Arquitetura**

✅ **Deploy automático** em todos os ambientes  
✅ **Isolamento completo** entre ambientes  
✅ **Configurações específicas** por ambiente  
✅ **Logs e monitoramento** independentes  
✅ **Rollback fácil** com Git tags  
✅ **Segurança** escalonada por ambiente  

---

🚀 **Sistema pronto para produção empresarial!**
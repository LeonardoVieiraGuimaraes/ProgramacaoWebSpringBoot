# 🚀 Guia Atualizado: Controle de Versões e Ambientes ProWeb

## 🌟 **Nova Estrutura de Branches**

```
🏭 PRODUÇÃO (main)        ←─── Deploy automático → proweb.leoproti.com.br:8013
    ↑
🧪 STAGING (staging)      ←─── Deploy automático → staging.proweb.leoproti.com.br:8020
    ↑
💻 DESENVOLVIMENTO (v02)  ←─── Execução local → localhost:8021
```

### 📊 **Tabela de Configurações**

| Branch | Ambiente | Execução | URL | Porta | Profile |
|--------|----------|----------|-----|-------|---------|
| `main` | **Produção** | Deploy Automático | https://proweb.leoproti.com.br | 8013 | `prod` |
| `staging` | **Homologação** | Deploy Automático | https://staging.proweb.leoproti.com.br | 8020 | `staging` |
| `v02` | **Desenvolvimento** | Local (sua máquina) | http://localhost:8021 | 8021 | `dev` |
| `v01` | **Versão Separada** | Manual | - | - | - |

---

## 🔄 **Fluxo de Trabalho Completo**

### **1. 💻 Desenvolvimento Local (v02)**

```bash
# Mudar para branch de desenvolvimento
git checkout v02
git pull origin v02

# Executar localmente (Windows)
./scripts/run-dev-local.ps1

# OU executar localmente (Linux/macOS)
./scripts/run-dev-local.sh
```

**✅ Resultado:**
- Aplicação roda em: http://localhost:8021
- Console H2: http://localhost:8021/h2-console
- Base de dados: `./h2data/devdb.mv.db`

### **2. 🧪 Promover para Homologação**

```bash
# Após desenvolvimento concluído
git add .
git commit -m "feat: nova funcionalidade implementada"

# Promover para staging
git checkout staging
git pull origin staging
git merge v02
git push origin staging
```

**✅ Resultado:**
- Deploy automático em: https://staging.proweb.leoproti.com.br:8020
- Workflow `ProWebDeploy.yml` executa automaticamente
- Base de dados: `/data/h2db/staging-db.mv.db` (servidor)

### **3. 🚀 Promover para Produção**

```bash
# Após testes aprovados em staging
git checkout main
git pull origin main
git merge staging

# Criar tag de versão
git tag -a v2.1.0 -m "Release v2.1.0: Nova funcionalidade"
git push origin main
git push origin v2.1.0
```

**✅ Resultado:**
- Deploy automático em: https://proweb.leoproti.com.br:8013
- Workflow `ProWebDeploy.yml` executa automaticamente
- Base de dados: `/data/h2db/production-db.mv.db` (servidor)

---

## 🛠️ **Scripts de Desenvolvimento**

### **Windows (PowerShell):**
```powershell
# Executar desenvolvimento local
.\scripts\run-dev-local.ps1
```

### **Linux/macOS (Bash):**
```bash
# Tornar executável (primeira vez)
chmod +x ./scripts/run-dev-local.sh

# Executar desenvolvimento local
./scripts/run-dev-local.sh
```

---

## 🎯 **Comandos Essenciais por Situação**

### **🔧 Desenvolvimento Diário:**
```bash
git checkout v02
git pull origin v02
# ... desenvolver localmente ...
git add . && git commit -m "sua mensagem"
git push origin v02  # Apenas salva, não faz deploy
```

### **🧪 Testar em Staging:**
```bash
git checkout staging
git merge v02
git push origin staging  # 🚀 Deploy automático em staging
```

### **🏆 Deploy em Produção:**
```bash
git checkout main
git merge staging
git tag v2.X.X
git push origin main --tags  # 🚀 Deploy automático em produção
```

### **🚨 Hotfix de Emergência:**
```bash
# Criar hotfix a partir da produção
git checkout main
git checkout -b hotfix/problema-critico

# Desenvolver correção localmente (se necessário)
git checkout v02
./scripts/run-dev-local.ps1
# ... testar correção ...

# Aplicar hotfix
git checkout hotfix/problema-critico
git add . && git commit -m "hotfix: correção crítica"

# Deploy direto em staging para testar
git checkout staging
git merge hotfix/problema-critico
git push origin staging  # Testar em staging primeiro

# Se OK, aplicar em produção
git checkout main
git merge staging
git tag v2.X.Y
git push origin main --tags
```

---

## 📊 **Monitoramento dos Ambientes**

### **Health Checks:**
```bash
# Desenvolvimento (local)
curl http://localhost:8021/actuator/health

# Staging (servidor)
curl https://staging.proweb.leoproti.com.br:8020/actuator/health

# Produção (servidor)
curl https://proweb.leoproti.com.br:8013/actuator/health
```

### **APIs de Teste:**
```bash
# Desenvolvimento
curl http://localhost:8021/produtos
curl http://localhost:8021/alunos

# Staging
curl https://staging.proweb.leoproti.com.br:8020/produtos
curl https://staging.proweb.leoproti.com.br:8020/alunos

# Produção
curl https://proweb.leoproti.com.br:8013/produtos
curl https://proweb.leoproti.com.br:8013/alunos
```

---

## 🔍 **Status e Verificações**

### **Verificar branches:**
```bash
git branch -a
git log --oneline --graph --decorate --all
```

### **Ver diferenças entre ambientes:**
```bash
git diff v02..staging     # O que será promovido para staging
git diff staging..main    # O que será promovido para produção
```

### **Verificar último deploy:**
```bash
git log staging --oneline -1    # Último commit em staging
git log main --oneline -1       # Último commit em produção
```

---

## 🎉 **Vantagens da Nova Estrutura**

### ✅ **Benefícios:**
- **Desenvolvimento rápido**: Execução local sem esperar deploy
- **Testes seguros**: Staging e produção isolados
- **Deploy automático**: Apenas push para staging/main
- **Economia de recursos**: Desenvolvimento não consome servidor
- **Flexibilidade**: v01 preservada como versão separada

### 🎯 **Fluxo Simplificado:**
1. **Desenvolva** em `v02` localmente
2. **Teste** promovendo para `staging` 
3. **Publique** promovendo para `main` (produção)

---

## 🚨 **Importantes:**

### **❌ Não faça:**
- Push direto em `main` (produção)
- Deploy em produção sem testar em staging
- Desenvolvimento direto em `staging` ou `main`

### **✅ Sempre faça:**
- Desenvolva em `v02`
- Teste em `staging` antes de produção
- Use tags para versões de produção
- Mantenha branches sincronizadas

---

## 📚 **Referência Rápida**

```bash
# DESENVOLVIMENTO
git checkout v02
./scripts/run-dev-local.ps1    # Windows
./scripts/run-dev-local.sh     # Linux/macOS

# STAGING
git checkout staging && git merge v02 && git push origin staging

# PRODUÇÃO  
git checkout main && git merge staging && git tag vX.Y.Z && git push origin main --tags
```

**🎯 Agora você tem controle total sobre desenvolvimento, homologação e produção!**
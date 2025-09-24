# 🐳 Docker por Branch - Configuração Multi-Ambiente

## 🎯 **Estratégia Implementada**

Cada branch tem seu próprio `docker-compose.yml` com configurações específicas. Quando fizer merge entre branches, as configurações Docker da **branch de destino** são mantidas.

## 📊 **Configurações por Branch**

### **🆓 Branch V01 (Versão Simples)**
```yaml
# docker-compose.yml da V01
image: proweb-v01-app
container_name: proweb-v01-app
ports: 8013:8013
environment:
  - SPRING_PROFILES_ACTIVE=v01
  - SERVER_PORT=8013
  - DATABASE: v01-db
  - H2_CONSOLE_ENABLED=true
networks: proweb-v01-network
```

### **🔧 Branch Staging (Homologação)**  
```yaml
# docker-compose.yml da Staging
image: proweb-staging-app
container_name: proweb-staging-app
ports: 8020:8020
environment:
  - SPRING_PROFILES_ACTIVE=staging
  - SERVER_PORT=8020
  - DATABASE: staging-db
  - LOG_LEVEL=DEBUG
  - H2_CONSOLE_ENABLED=true
networks: proweb-staging-network
```

### **🚀 Branch Main (Produção)**
```yaml
# docker-compose.yml da Main
image: proweb-prod-app
container_name: proweb-prod-app
ports: 8021:8021
environment:
  - SPRING_PROFILES_ACTIVE=prod
  - SERVER_PORT=8021
  - DATABASE: prod-db
  - LOG_LEVEL=INFO
  - H2_CONSOLE_ENABLED=false
networks: proweb-prod-network
```

## 🔄 **Como Funciona o Merge**

### **Exemplo: V01 → Staging**

#### **1. Estado Inicial:**
- **V01**: Docker na porta 8013
- **Staging**: Docker na porta 8020

#### **2. Fazendo o Merge:**
```bash
git checkout staging
git merge v01
```

#### **3. Resultado:**
- ✅ **Código da V01** é incorporado na staging
- ✅ **Docker da Staging** é mantido (porta 8020)
- ✅ **Configurações específicas** da staging preservadas

### **Processo Automático:**
1. **Conflito detectado** no `docker-compose.yml`
2. **Git escolhe a versão da branch destino** (`--ours`)
3. **Código é mergeado**, Docker permanece específico

## 🎯 **Benefícios**

### **✅ Isolamento Completo:**
- **V01**: Porta 8013, profile v01, debug habilitado
- **Staging**: Porta 8020, profile staging, logs debug
- **Produção**: Porta 8021, profile prod, logs otimizados

### **✅ Deploy Seguro:**
- **Cada ambiente** mantém suas configurações
- **Merge de código** não afeta configurações Docker
- **CI/CD** usa as configurações corretas automaticamente

### **✅ Desenvolvimento Flexível:**
- **Desenvolver na V01** com configurações de desenvolvimento
- **Testar na Staging** com configurações de homologação  
- **Deploy em Produção** com configurações otimizadas

## 🚀 **Fluxo de Trabalho**

### **📋 Desenvolvimento:**
```bash
# Trabalhar na V01
git checkout v01
# ... desenvolver funcionalidades ...
git commit -m "feat: nova funcionalidade"

# Testar localmente
docker-compose up  # Roda na porta 8013
```

### **🔧 Homologação:**
```bash
# Merge para staging
git checkout staging
git merge v01  # Mantém configurações da staging (porta 8020)
git push origin staging  # Deploy automático na porta 8020
```

### **🚀 Produção:**
```bash
# Merge para main
git checkout main  
git merge staging  # Mantém configurações da main (porta 8021)
git push origin main  # Deploy automático na porta 8021
```

## 🔍 **Verificação do Comportamento**

### **Testar o Merge:**
```bash
# V01 → Staging (código da v01, docker da staging)
git log --oneline -5  # Ver histórico do merge
docker-compose config  # Verificar configurações resultantes
```

### **URLs de Teste:**
- **V01**: http://localhost:8013 (desenvolvimento)
- **Staging**: http://localhost:8020 (homologação)
- **Produção**: http://localhost:8021 (produção)

## ⚠️ **Pontos Importantes**

### **🔧 Resolução de Conflitos:**
- **Sempre manter** configurações Docker da branch de destino
- **Usar `--ours`** para manter configurações da branch atual
- **Código é mergeado**, ambiente permanece isolado

### **📦 CI/CD Compatível:**
- **Workflow GitHub Actions** usa configurações dinâmicas
- **Deploy automático** detecta a branch e usa porta correta
- **Ambientes isolados** no servidor remoto

## 🎉 **Resultado Final**

✅ **Cada branch** tem seu Docker específico
✅ **Merge preserva** configurações de ambiente  
✅ **Deploy automático** usa configurações corretas
✅ **Desenvolvimento isolado** e seguro

**Estratégia implementada com sucesso!** 🚀
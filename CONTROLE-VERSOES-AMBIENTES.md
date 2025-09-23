# 📚 Guia Completo: Controle de Versões e Ambientes

## 🌟 **Visão Geral do Sistema**

Seu projeto usa uma estratégia **GitFlow adaptada** com 3 ambientes automatizados:

```
🏭 PRODUÇÃO (v01)     ←─── Ambiente final dos usuários
    ↑
🧪 STAGING (main)     ←─── Testes finais / Homologação  
    ↑
💻 DESENVOLVIMENTO    ←─── Desenvolvimento ativo
   (v02/feature/*)
```

---

## 🌿 **Estratégia de Branches**

### 📊 **Tabela de Branches e Ambientes**

| Branch | Ambiente | Porta | Auto-Deploy | Proteção | Uso |
|--------|----------|-------|-------------|----------|-----|
| `v01` | **Produção** | 8013 | ✅ Sim | 🔒 Alta | Versão estável para usuários |
| `main` | **Staging** | 8020 | ✅ Sim | 🔐 Média | Homologação/testes finais |
| `v02` | **Dev Principal** | 8021 | ✅ Sim | 🔓 Baixa | Desenvolvimento principal |
| `feature/*` | **Desenvolvimento** | 8021 | ✅ Sim | 🔓 Baixa | Features específicas |

---

## 🚀 **Como Trabalhar com Versões**

### 🔧 **1. Desenvolvimento de Nova Feature**

```bash
# 1. Sempre parta do desenvolvimento principal
git checkout v02
git pull origin v02

# 2. Crie uma branch para sua feature
git checkout -b feature/login-sistema
# ou
git checkout -b feature/crud-produtos
# ou  
git checkout -b feature/api-relatorios

# 3. Desenvolva sua feature
# ... código, testes, commits ...
git add .
git commit -m "✨ Implementa sistema de login"

# 4. Push dispara deploy automático em DEV
git push origin feature/login-sistema
# 🎯 Deploy automático em: https://dev.prowebv01.leoproti.com.br
```

### 🧪 **2. Promover para Homologação (Staging)**

```bash
# 1. Após feature testada em dev, merge para v02
git checkout v02
git merge feature/login-sistema
git push origin v02

# 2. Quando conjunto de features estiver pronto, promover para staging
git checkout main
git merge v02
git push origin main
# 🎯 Deploy automático em: https://staging.prowebv01.leoproti.com.br
```

### 🏆 **3. Promover para Produção**

```bash
# 1. Após testes em staging aprovados
git checkout v01
git merge main

# 2. Criar tag de versão (versionamento semântico)
git tag v1.2.0  # Major.Minor.Patch
git push origin v01 --tags
# 🎯 Deploy automático em: https://prowebv01.leoproti.com.br
```

---

## 🏷️ **Sistema de Versionamento Semântico**

### 📋 **Formato: `vMAJOR.MINOR.PATCH`**

```bash
v1.0.0  # Primeira versão em produção
v1.0.1  # Correção de bug
v1.1.0  # Nova funcionalidade 
v1.1.1  # Correção de bug
v2.0.0  # Mudança breaking (incompatível)
```

### 🎯 **Quando usar cada tipo:**

- **PATCH (1.0.X)** - Correções de bugs, sem novas features
- **MINOR (1.X.0)** - Novas funcionalidades, mantém compatibilidade
- **MAJOR (X.0.0)** - Mudanças que quebram compatibilidade

### 💻 **Comandos para Versionamento:**

```bash
# Listar tags existentes
git tag -l

# Criar tag de patch (correção)
git tag v1.0.1
git push origin v1.0.1

# Criar tag de minor (nova feature)
git tag v1.1.0
git push origin v1.1.0

# Criar tag de major (breaking change)
git tag v2.0.0
git push origin v2.0.0

# Ver histórico de versões
git log --oneline --graph --decorate --tags
```

---

## 🔄 **Fluxos de Trabalho Comuns**

### 🐛 **Hotfix Urgente em Produção**

```bash
# 1. Criar branch de hotfix direto da produção
git checkout v01
git checkout -b hotfix/correcao-critica

# 2. Implementar correção
git add .
git commit -m "🚑 Hotfix: corrige bug crítico de segurança"

# 3. Merge direto em produção
git checkout v01
git merge hotfix/correcao-critica
git tag v1.0.1  # Versão de patch
git push origin v01 --tags

# 4. Propagar correção para outras branches
git checkout main
git merge v01
git push origin main

git checkout v02  
git merge main
git push origin v02
```

### 🎉 **Release de Nova Versão**

```bash
# 1. Finalizar desenvolvimento em v02
git checkout v02
git commit -m "🔖 Prepara release v1.2.0"

# 2. Promover para staging
git checkout main
git merge v02
git push origin main
# ⏳ Aguardar testes em staging

# 3. Após aprovação, promover para produção
git checkout v01
git merge main
git tag v1.2.0
git push origin v01 --tags
```

### 🔄 **Sincronizar Feature Branch**

```bash
# Manter feature atualizada com desenvolvimento principal
git checkout feature/minha-feature
git merge v02  # ou git rebase v02
git push origin feature/minha-feature
```

---

## 📊 **Monitoramento e Controle**

### 🖥️ **URLs dos Ambientes**

```bash
# Verificar status de cada ambiente
curl https://prowebv01.leoproti.com.br/actuator/health          # Produção
curl https://staging.prowebv01.leoproti.com.br/actuator/health  # Staging  
curl https://dev.prowebv01.leoproti.com.br/actuator/health      # Desenvolvimento
```

### 📈 **Comandos de Monitoramento**

```bash
# Ver todas as branches
git branch -a

# Ver status atual
git status

# Ver histórico com tags
git log --oneline --graph --decorate --all

# Ver diferenças entre branches
git diff main..v01        # Diferenças entre staging e produção
git diff v02..main        # Diferenças entre dev e staging

# Ver último deploy por ambiente
git log --oneline -1 v01   # Última versão em produção
git log --oneline -1 main  # Última versão em staging
git log --oneline -1 v02   # Última versão em desenvolvimento
```

---

## ⚡ **Comandos Rápidos por Situação**

### 🎯 **Desenvolvimento Normal**
```bash
git checkout v02
git pull origin v02
git checkout -b feature/nova-funcionalidade
# ... desenvolver ...
git push origin feature/nova-funcionalidade
```

### 🧪 **Testar em Staging**  
```bash
git checkout main
git merge v02
git push origin main
```

### 🏆 **Deploy em Produção**
```bash
git checkout v01
git merge main  
git tag v1.X.X
git push origin v01 --tags
```

### 🔄 **Sincronizar tudo**
```bash
git checkout v02 && git pull origin v02
git checkout main && git pull origin main  
git checkout v01 && git pull origin v01
```

---

## 🛠️ **Ferramentas de Apoio**

### 📝 **Aliases Úteis para Git**

```bash
# Adicionar ao ~/.gitconfig
git config --global alias.co checkout
git config --global alias.br branch
git config --global alias.ci commit
git config --global alias.st status
git config --global alias.lg "log --oneline --graph --decorate --all"
git config --global alias.sync "!f() { git checkout \$1 && git pull origin \$1; }; f"
```

### 🔍 **Scripts de Verificação**

```bash
# Verificar se branches estão sincronizadas
git log --oneline --graph v01..main    # O que tem em staging que não está em produção
git log --oneline --graph main..v02    # O que tem em dev que não está em staging
```

---

## 🎯 **Boas Práticas**

### ✅ **DO (Faça)**
- ✅ Sempre teste em dev antes de staging
- ✅ Sempre teste em staging antes de produção  
- ✅ Use mensagens de commit descritivas
- ✅ Crie tags para todas as versões de produção
- ✅ Mantenha branches sincronizadas
- ✅ Use branches feature/ para desenvolvimento
- ✅ Faça commits pequenos e frequentes

### ❌ **DON'T (Não faça)**
- ❌ Nunca faça commit direto em v01 (produção)
- ❌ Não pule ambientes (dev → produção direto)
- ❌ Não deixe features muito antigas em branches separadas
- ❌ Não force push em branches compartilhadas
- ❌ Não misture features diferentes em uma branch
- ❌ Não esqueça de fazer pull antes de começar
- ❌ Não faça merge sem testar

---

## 🚨 **Resolução de Problemas**

### 🔧 **Branch fora de sincronia**
```bash
git checkout nome-da-branch
git pull origin nome-da-branch
git merge v02  # Sincronizar com desenvolvimento principal
```

### 🔄 **Reverter deploy com problema**
```bash
# Voltar para versão anterior
git checkout v01
git reset --hard v1.1.0  # Voltar para tag específica
git push origin v01 --force-with-lease
```

### 🧹 **Limpeza de branches antigas**
```bash
# Listar branches merged
git branch --merged main

# Deletar branch local
git branch -d feature/funcionalidade-antiga

# Deletar branch remota  
git push origin --delete feature/funcionalidade-antiga
```

---

## 🎉 **Resumo dos Comandos Essenciais**

```bash
# 🚀 DESENVOLVIMENTO DIÁRIO
git checkout v02
git pull origin v02
git checkout -b feature/minha-feature
git push origin feature/minha-feature

# 🧪 PROMOVER PARA STAGING
git checkout main
git merge v02
git push origin main

# 🏆 PROMOVER PARA PRODUÇÃO  
git checkout v01
git merge main
git tag v1.X.X
git push origin v01 --tags

# 📊 MONITORAMENTO
git status
git log --oneline --graph --decorate --all
git branch -a
```

---

Agora você tem o controle completo! 🎯

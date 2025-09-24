# 🚀 Controle de Versões - Spring Boot Project

Este projeto agora possui um **sistema completo de controle de versões** com diferentes funcionalidades organizadas em branches.

---

## 🌳 **Estrutura das Branches**

```
📦 ProgramacaoWebSpringBoot
├── 🌟 main      (Base do projeto)
├── 🔧 staging   (Ambiente de testes)
├── 🔒 v02       (Versão com Autenticação JWT)
└── 🆓 v01       (Versão Simples sem Autenticação)
```

---

## 📊 **Comparativo das Versões**

| Característica | V01 (Simples) | V02 (Completa) |
|----------------|---------------|----------------|
| **🌐 Porta** | `8013` | `8080` |
| **🔒 Autenticação** | ❌ Não | ✅ Spring Security + JWT |
| **👥 Usuários** | ❌ Não | ✅ Cadastro/Login |
| **🛡️ Roles** | ❌ Não | ✅ ADMIN/MANAGER/USER |
| **🔐 Endpoints Protegidos** | ❌ Não | ✅ `/api/*`, `/admin/*` |
| **📊 Complexidade** | 🟢 Baixa | 🟡 Média/Alta |
| **🎯 Uso Ideal** | Desenvolvimento/Testes | Produção |
| **📦 Dependências** | Mínimas | Completas |

---

## 🔄 **Como Alternar Entre Versões**

### **➡️ Para V01 (Simples)**
```bash
git checkout v01
./run-v01.bat

# URLs V01:
# 🌐 App: http://localhost:8013
# 📖 Swagger: http://localhost:8013/swagger-ui/index.html
# 🗄️ H2: http://localhost:8013/h2-console
```

### **➡️ Para V02 (Com Autenticação)**
```bash
git checkout v02
./run-auth.bat

# URLs V02:
# 🌐 App: http://localhost:8080
# 📖 Swagger: http://localhost:8080/swagger-ui/index.html
# 🗄️ H2: http://localhost:8080/h2-console
```

---

## 🛠️ **Comandos Úteis**

### **📋 Verificar Status**
```bash
# Ver branch atual
git branch

# Ver todas as branches com commits
git branch -v

# Ver status da branch atual
git status
```

### **🔄 Navegação**
```bash
# Alternar para V01
git checkout v01

# Alternar para V02
git checkout v02

# Voltar para main
git checkout main

# Ver diferenças entre branches
git diff v01 v02
```

### **💾 Backup e Segurança**
```bash
# Fazer backup antes de mudanças
git tag backup-$(date +%Y%m%d)

# Criar nova branch baseada na atual
git checkout -b nova-feature

# Fazer merge de features
git checkout v01
git merge nova-feature
```

---

## 🎯 **Cenários de Uso**

### **🟢 Use V01 quando:**
- ✅ Desenvolvimento inicial
- ✅ Prototipação rápida
- ✅ Testes de funcionalidades básicas
- ✅ Demonstrações simples
- ✅ Aprendizado de Spring Boot

### **🟡 Use V02 quando:**
- ✅ Ambiente de produção
- ✅ Necessidade de segurança
- ✅ Controle de acesso por roles
- ✅ Sistema completo
- ✅ Deploy final

---

## 📁 **Arquivos Específicos por Versão**

### **V01 (Simples)**
```
✅ README_V01.md
✅ run-v01.bat
✅ Porta 8013
✅ Sem arquivos de segurança
✅ Configuração simplificada
```

### **V02 (Completa)**
```
✅ README_AUTENTICACAO.md
✅ run-auth.bat
✅ init-users.sql
✅ Porta 8080
✅ SecurityConfig.java
✅ JwtTokenUtil.java
✅ AuthController.java
✅ UserManagementController.java
✅ User.java, Role.java
✅ DTOs de autenticação
```

---

## 🚀 **Execução Rápida**

### **Scripts Prontos:**
```bash
# V01 - Simples
./run-v01.bat

# V02 - Com autenticação
./run-auth.bat
```

### **Comandos Maven:**
```bash
# V01
# Versão Simples (V01)
git checkout v01 && mvn spring-boot:run

# V02  
git checkout v02 && mvn spring-boot:run
```

---

## 🔐 **Credenciais V02**

Quando usar a V02, utilize estas credenciais:

| Usuário | Senha | Role |
|---------|-------|------|
| `admin` | `123456` | ADMIN |
| `manager` | `123456` | MANAGER |
| `user` | `123456` | USER |
| `leonardo` | `123456` | ADMIN |

---

## 📈 **Vantagens do Controle de Versões**

### **🎯 Organização**
- ✅ **Separação clara** entre versões
- ✅ **Histórico preservado** de cada versão
- ✅ **Facilidade de alternância** entre versões
- ✅ **Desenvolvimento paralelo** possível

### **🔒 Segurança**
- ✅ **Backup automático** via Git
- ✅ **Rollback fácil** se necessário
- ✅ **Branches isoladas** evitam conflitos
- ✅ **Tags de versão** para marcos importantes

### **👥 Colaboração**
- ✅ **Múltiplos desenvolvedores** podem trabalhar
- ✅ **Features independentes** em branches separadas
- ✅ **Merge controlado** quando pronto
- ✅ **Review de código** facilitado

---

## 🎉 **Próximos Passos**

1. **✅ Testar ambas as versões**
2. **✅ Criar documentação específica**
3. **✅ Configurar CI/CD por branch**
4. **✅ Fazer deploy de cada versão**
5. **✅ Monitorar performance**

---

> 🌟 **Parabéns!** Agora você tem controle total sobre as versões do seu projeto Spring Boot!  
> 📅 **Data:** 23/09/2025  
> 👨‍💻 **Autor:** Leonardo Vieira Guimarães
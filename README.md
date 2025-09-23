# 🚀 ProWeb - Sistema Spring Boot

Sistema web desenvolvido em Spring Boot com controle de versões profissional e deploy automático.

## 🏗️ **Arquitetura de Ambientes**

| Branch | Ambiente | Execução | URL | Porta |
|--------|----------|----------|-----|-------|
| `v02` | **Desenvolvimento** | Local | `localhost:8021` | 8021 |
| `staging` | **Homologação** | Deploy Automático | `staging.proweb.leoproti.com.br` | 8020 |
| `main` | **Produção** | Deploy Automático | `proweb.leoproti.com.br` | 8013 |

## 🚀 **Como Usar**

### **Desenvolvimento Local:**
```bash
# Windows
.\scripts\run-dev-local.ps1

# Linux/macOS
./scripts/run-dev-local.sh
```

### **Promover para Staging:**
```bash
git checkout staging
git merge v02
git push origin staging
```

### **Promover para Produção:**
```bash
git checkout main
git merge staging
git tag v2.X.X
git push origin main --tags
```

## 📚 **Documentação**

- **[NOVO-CONTROLE-VERSOES.md](./NOVO-CONTROLE-VERSOES.md)** - Guia completo de controle de versões
- **[TROUBLESHOOTING-H2.md](./TROUBLESHOOTING-H2.md)** - Solução de problemas do H2

## 🛠️ **Tecnologias**

- **Spring Boot 3.4.5**
- **Java 21**
- **H2 Database**
- **Docker**
- **GitHub Actions**
- **Cloudflare**

## 📊 **APIs Disponíveis**

- `/produtos` - CRUD de produtos
- `/alunos` - CRUD de alunos
- `/actuator/health` - Health check
- `/h2-console` - Console H2 (apenas desenvolvimento)

## 🎯 **Fluxo de Trabalho**

1. **Desenvolva** em `v02` localmente
2. **Teste** em `staging` (deploy automático)
3. **Publique** em `main` (produção - deploy automático)

---

**ProWeb** - Sistema profissional com DevOps automatizado 🚀
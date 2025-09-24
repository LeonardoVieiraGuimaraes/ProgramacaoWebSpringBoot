# 🚀 ProWeb - Sistema Spring Boot

Sistema web desenvolvido em Spring Boot com controle de versões profissional e deploy automático multi-ambiente.

## 🏗️ **Arquitetura de Ambientes**

| Branch | Ambiente | Execução | URL | Porta | Profile |
|--------|----------|----------|-----|-------|---------|
| `v02` | **Desenvolvimento** | Local | `localhost:8022` | 8022 | `dev` |
| `staging` | **Homologação** | Deploy Automático | `staging.proweb.leoproti.com.br` | 8020 | `staging` |
| `main` | **Produção** | Deploy Automático | `proweb.leoproti.com.br` | 8021 | `prod` |

## � **GitFlow - Fluxo de Trabalho**

```
💻 DESENVOLVIMENTO (v02)  ←─── Execução local → localhost:8022
           ↓ merge
🧪 STAGING (staging)      ←─── Deploy automático → staging.proweb.leoproti.com.br:8020
           ↓ merge + tag
🏭 PRODUÇÃO (main)        ←─── Deploy automático → proweb.leoproti.com.br:8021
```

### **📋 Processo de Desenvolvimento**

#### 1. **Desenvolvimento Local (v02)**
```bash
# Mudar para branch de desenvolvimento
git checkout v02
git pull origin v02

# Executar localmente
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8022 --spring.profiles.active=dev"

# Acessar aplicação
# 🌐 http://localhost:8022
# 🔧 http://localhost:8022/h2-console
```

#### 2. **Deploy para Staging**
```bash
# Commit suas alterações
git add .
git commit -m "feat: nova funcionalidade"
git push origin v02

# Promover para staging
git checkout staging
git merge v02
git push origin staging  # 🚀 Deploy automático executado
```

#### 3. **Deploy para Produção**
```bash
# Promover staging para produção
git checkout main
git merge staging
git tag v1.2.3
git push origin main --tags  # 🚀 Deploy automático executado
```

## �️ **Desenvolvimento Local**

### **Configurações por Ambiente**

#### 🔧 **Desenvolvimento (port 8022)**
- Profile: `dev`
- Banco: H2 file (`./h2data/devdb`)
- Console H2: habilitado
- Logs: DEBUG
- Hot reload: habilitado

#### 🧪 **Staging (port 8020)**
- Profile: `staging`
- Banco: H2 file (`./h2data/stagingdb`)
- Console H2: habilitado (apenas staging)
- Logs: INFO
- Validação completa

#### 🏭 **Produção (port 8021)**
- Profile: `prod`
- Banco: H2 file (`./h2data/proddb`)
- Console H2: **desabilitado**
- Logs: WARN
- Otimizações de performance

### **Comandos Essenciais**

```bash
# Compilar projeto
mvn clean package -DskipTests

# Executar desenvolvimento
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8022 --spring.profiles.active=dev"

# Executar testes
mvn test

# Gerar JAR
mvn clean package
```

## 🚀 **Deploy Automático**

### **GitHub Actions Workflow**

O deploy é automatizado via GitHub Actions (`ProWebDeploy.yml`):

- **Staging**: Deploy automático ao fazer push para `staging`
- **Produção**: Deploy automático ao fazer push para `main`

### **Processo de Deploy**

1. **Build**: Compilação do projeto Maven
2. **Test**: Execução de testes automatizados
3. **Package**: Geração do JAR
4. **Deploy**: Upload e execução no servidor
5. **Health Check**: Verificação da aplicação

## 📊 **Monitoramento**

### **Health Checks**

```bash
# Desenvolvimento
curl http://localhost:8022/actuator/health

# Staging
curl https://staging.proweb.leoproti.com.br:8020/actuator/health

# Produção
curl https://proweb.leoproti.com.br:8021/actuator/health
```

### **APIs Disponíveis**

```bash
# Produtos
curl http://localhost:8022/produtos
curl https://staging.proweb.leoproti.com.br:8020/produtos
curl https://proweb.leoproti.com.br:8021/produtos

# Alunos
curl http://localhost:8022/alunos
curl https://staging.proweb.leoproti.com.br:8020/alunos
curl https://proweb.leoproti.com.br:8021/alunos
```

## 🛠️ **Tecnologias**

- **Spring Boot 3.4.5** - Framework principal
- **Java 21** - Runtime
- **H2 Database** - Banco de dados
- **Maven** - Gerenciador de dependências
- **Docker** - Containerização
- **GitHub Actions** - CI/CD
- **Cloudflare** - Proxy HTTPS

## 📁 **Estrutura do Projeto**

```
src/
├── main/
│   ├── java/com/example/deploy/
│   │   ├── controller/     # Controllers REST/MVC
│   │   ├── model/          # Entidades JPA
│   │   ├── repository/     # Repositórios Spring Data
│   │   ├── service/        # Lógica de negócio
│   │   └── config/         # Configurações
│   └── resources/
│       ├── application.yaml          # Config base
│       ├── application-dev.yaml      # Config desenvolvimento
│       ├── application-staging.yml   # Config staging
│       ├── application-prod.yaml     # Config produção
│       └── templates/                # Templates Thymeleaf
└── test/                             # Testes automatizados
```

## ⚠️ **Troubleshooting**

### **Problemas Comuns**

#### **Porta em uso**
```bash
# Windows
netstat -ano | findstr :8022
taskkill /PID <PID> /F

# Linux/macOS
lsof -ti:8022 | xargs kill
```

#### **Banco H2 travado**
1. Parar aplicação
2. Remover arquivo `h2data/*.db`
3. Reiniciar aplicação

#### **Deploy falhou**
1. Verificar logs no GitHub Actions
2. Verificar conectividade SSH
3. Verificar espaço em disco no servidor

### **Logs de Debug**

```bash
# Habilitar logs detalhados
mvn spring-boot:run -Dspring-boot.run.arguments="--logging.level.com.example.deploy=DEBUG"
```

## 🔐 **Segurança**

- **CORS** configurado para domínios permitidos
- **Headers de segurança** habilitados
- **Console H2** desabilitado em produção
- **HTTPS** obrigatório via Cloudflare

## 📝 **Changelog**

### **v2.0.0** (Atual)
- ✅ Controle de versões GitFlow
- ✅ Deploy automático multi-ambiente
- ✅ Configuração por profiles
- ✅ Monitoramento com Actuator
- ✅ CI/CD completo

---

**🚀 Sistema pronto para desenvolvimento profissional!**
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
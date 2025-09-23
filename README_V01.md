# 🌟 Spring Boot V01 - Versão Simples

Este é o **Spring Boot na versão 01** - uma versão **simplificada sem autenticação** para desenvolvimento e prototipação rápida.

---

## 🎯 **Características da V01**

### ✅ **O que TEM:**
- ✅ **API REST** para Alunos e Produtos
- ✅ **Banco H2** em memória para testes
- ✅ **Swagger UI** para documentação
- ✅ **Thymeleaf** para páginas web
- ✅ **CORS** configurado
- ✅ **Actuator** para monitoramento
- ✅ **DevTools** para reload automático

### ❌ **O que NÃO TEM:**
- ❌ **Spring Security** (sem autenticação)
- ❌ **JWT** (sem tokens)
- ❌ **Roles/Permissões** (sem controle de acesso)
- ❌ **Usuários** (sem cadastro de usuários)

---

## 🚀 **Como Executar**

### **Opção 1: Script Automático**
```batch
run-v01.bat
```

### **Opção 2: Maven Direto**
```bash
mvn spring-boot:run
```

### **Opção 3: Com Configurações Customizadas**
```bash
# Alterar porta
mvn spring-boot:run -Dserver.port=8020

# Alterar perfil
mvn spring-boot:run -Dspring.profiles.active=dev
```

---

## 🌐 **URLs de Acesso**

| Recurso | URL | Descrição |
|---------|-----|-----------|
| **API Principal** | http://localhost:8021 | Página inicial |
| **Swagger UI** | http://localhost:8021/swagger-ui/index.html | Documentação interativa |
| **H2 Console** | http://localhost:8021/h2-console | Interface do banco H2 |
| **Health Check** | http://localhost:8021/actuator/health | Status da aplicação |
| **Metrics** | http://localhost:8021/actuator/metrics | Métricas da aplicação |

---

## 🗄️ **Configuração do Banco H2**

Para acessar o **H2 Console** em http://localhost:8021/h2-console:

```properties
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (deixar vazio)
```

---

## 📡 **Endpoints da API**

### **🎓 Alunos**
```bash
# Listar todos
GET /api/alunos

# Buscar por ID
GET /api/alunos/{id}

# Criar novo
POST /api/alunos

# Atualizar
PUT /api/alunos/{id}

# Deletar
DELETE /api/alunos/{id}
```

### **📦 Produtos**
```bash
# Listar todos
GET /api/produtos

# Buscar por ID
GET /api/produtos/{id}

# Criar novo
POST /api/produtos

# Atualizar
PUT /api/produtos/{id}

# Deletar
DELETE /api/produtos/{id}
```

---

## 🧪 **Testes com cURL**

### **Testar Alunos:**
```bash
# Criar aluno
curl -X POST http://localhost:8021/api/alunos \
  -H "Content-Type: application/json" \
  -d '{"nome":"João Silva","email":"joao@exemplo.com","idade":22,"curso":"Engenharia"}'

# Listar alunos
curl http://localhost:8021/api/alunos
```

### **Testar Produtos:**
```bash
# Criar produto
curl -X POST http://localhost:8021/api/produtos \
  -H "Content-Type: application/json" \
  -d '{"nome":"Notebook","preco":2500.00,"descricao":"Notebook Dell"}'

# Listar produtos
curl http://localhost:8021/api/produtos
```

---

## ⚙️ **Configurações**

### **Porta Padrão:** 8021
```yaml
server:
  port: 8021
```

### **Variáveis de Ambiente:**
```bash
SERVER_PORT=8021                    # Porta do servidor
SPRING_DATASOURCE_URL=...          # URL do banco
H2_CONSOLE_ENABLED=true             # Habilitar H2 Console
LOG_LEVEL=INFO                      # Nível de log
```

---

## 🔄 **Alternando entre Versões**

### **Para V02 (com autenticação):**
```bash
git checkout v02
mvn spring-boot:run
```

### **Para V01 (sem autenticação):**
```bash
git checkout v01
mvn spring-boot:run
```

---

## 📊 **Comparação V01 vs V02**

| Recurso | V01 | V02 |
|---------|-----|-----|
| **Porta Padrão** | 8021 | 8080 |
| **Autenticação** | ❌ Não | ✅ JWT |
| **Usuários** | ❌ Não | ✅ Sim |
| **Roles** | ❌ Não | ✅ ADMIN/MANAGER/USER |
| **Endpoints Protegidos** | ❌ Não | ✅ Sim |
| **Complexidade** | 🟢 Baixa | 🟡 Média |
| **Ideal para** | Desenvolvimento/Testes | Produção |

---

## 🐳 **Docker (Futuro)**

```yaml
# docker-compose.yml para V01
version: '3.8'
services:
  spring-v01:
    build: .
    ports:
      - "8021:8021"
    environment:
      - SERVER_PORT=8021
      - SPRING_PROFILES_ACTIVE=dev
```

---

## 📞 **Suporte**

- **📧 Email:** leonardo@leoproti.com.br
- **🐙 GitHub:** [LeonardoVieiraGuimaraes](https://github.com/LeonardoVieiraGuimaraes)

---

> 🌟 **Versão:** V01 (Simples)  
> 📅 **Data:** 23/09/2025  
> 👨‍💻 **Autor:** Leonardo Vieira Guimarães
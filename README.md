# ProWebV01 - Spring Boot CRUD Completo

Este projeto foi desenvolvido para fins didáticos nas disciplinas de Programação Web e Arquitetura de Aplicações Web, demonstrando a construção de APIs REST e aplicações web completas com Spring Boot, integração com banco de dados, documentação automática e deploy automatizado.

**Homeserver configurado para:** `prowebv01.leoproti.com.br`

---

## Funcionalidades

- **Hello World:**  
  Primeira rota criada para testar o funcionamento do projeto e do Spring Boot.
- **CRUD de Produtos:**  
  API REST e interface web (Thymeleaf) para cadastro, listagem, edição e exclusão de produtos (nome, preço).
- **CRUD de Alunos:**  
  API REST e interface web (Thymeleaf) para cadastro, listagem, edição e exclusão de alunos (nome, turma, curso, matrícula).
- **Documentação Swagger/OpenAPI:**  
  Interface automática para explorar e testar os endpoints REST.
- **Deploy automatizado:**  
  Pipeline GitHub Actions para build e deploy contínuo no servidor remoto.
- **Banco de dados:**  
  H2 em memória para desenvolvimento e H2 em arquivo para produção (via Docker).
- **CORS configurado:**  
  Suporte para acesso do domínio `prowebv01.leoproti.com.br`.

## Deploy e Configuração do Homeserver

### Configuração Atual
- **Domínio:** prowebv01.leoproti.com.br
- **Porta:** 8013
- **Perfil:** Produção
- **Banco:** H2 (arquivo persistente)
- **Deploy:** GitHub Actions automatizado

### Scripts de Verificação

#### Verificação de Permissões H2 (Recomendado antes do deploy):

**Windows PowerShell:**
```powershell
.\check-h2-permissions.ps1
```

**Linux/macOS:**
```bash
chmod +x check-h2-permissions.sh
./check-h2-permissions.sh
```

### Deploy Manual:
```bash
# Parar containers existentes
docker compose down

# Construir e iniciar
docker compose up --build -d

# Verificar status
docker compose ps

# Acompanhar logs
docker compose logs -f app
```

### Troubleshooting H2
Para problemas com o banco de dados H2 (AccessDeniedException, container reiniciando), consulte:
- **[TROUBLESHOOTING-H2.md](./TROUBLESHOOTING-H2.md)** - Guia completo de solução de problemas

### GitHub Actions Deploy
O deploy é automatizado via GitHub Actions:
- **Trigger:** Push na branch `main`
- **Pipeline:** Build → Deploy → Verificação automática
- **Correções:** Permissões H2 corrigidas automaticamente
- **Logs:** Disponíveis na aba Actions do GitHub

### Configurações de Produção
- Console H2 desabilitado por segurança
- Logs estruturados com rotação automática
- Health checks configurados
- CORS restritivo para domínio específico
- Usuário container com UID 1000 para compatibilidade
- Volume H2 com permissões específicas (755)

---

## Estrutura do Projeto

- **/src/main/java/com/example/deploy/controller**  
  Controllers REST e web (Thymeleaf) para Produtos e Alunos.
- **/src/main/java/com/example/deploy/model**  
  Entidades JPA: Produto e Aluno.
- **/src/main/java/com/example/deploy/repository**  
  Repositórios JPA para Produto e Aluno.
- **/src/main/java/com/example/deploy/service**  
  Serviços de negócio para Produto e Aluno.
- **/src/main/resources/templates/**  
  Views Thymeleaf para Produtos e Alunos.
- **/src/main/resources/static/**  
  Frontend HTML/JS para consumir a API REST.
- **/src/main/resources/application.yaml**  
  Configuração padrão para desenvolvimento (H2).
- **/docker-compose.yml**  
  Orquestração dos containers Spring Boot e MariaDB para produção.
- **/Dockerfile.spring**  
  Build da imagem da aplicação Spring Boot.
- **/Dockerfile.mysql**  
  Build da imagem do banco MariaDB.
- **/.github/workflows/deploy.yml**  
  Pipeline de CI/CD para build e deploy automático.

---

## Dependências Utilizadas

- **Spring Boot Starter Web**  
  Para criação de APIs REST e controllers web.
- **Spring Boot Starter Data JPA**  
  Integração com bancos de dados relacionais via JPA/Hibernate.
- **Spring Boot Starter Thymeleaf**  
  Renderização de páginas HTML dinâmicas.
- **Spring Boot DevTools**  
  Hot reload para desenvolvimento.
- **MariaDB Java Client**  
  Driver JDBC para conexão com MariaDB.
- **H2 Database**  
  Banco de dados em memória para desenvolvimento e testes.
- **Spring Boot Starter Test**  
  Dependências para testes automatizados.
- **SpringDoc OpenAPI Starter WebMVC UI**  
  Geração automática da documentação Swagger/OpenAPI.
- **Maven Compiler Plugin**  
  Compilação do projeto com suporte ao Java 24.
- **Spring Boot Maven Plugin**  
  Empacotamento e execução da aplicação Spring Boot.

---

## Como Executar

### Desenvolvimento Local (H2)

1. Clone o repositório.
2. Execute `mvn clean install` para compilar.
3. Rode a aplicação com `mvn spring-boot:run`.
4. Acesse:
   - API REST: `http://localhost:8080/produtos` e `http://localhost:8080/alunos`
   - Interface web: `http://localhost:8080/produtos-view` e `http://localhost:8080/alunos-view`
   - Swagger: `http://localhost:8080/swagger-ui.html` ou `/swagger-ui/index.html`
   - Health check: `http://localhost:8080/actuator/health`

### Produção (H2 persistente via Docker)

1. **Verificar permissões primeiro** (recomendado):
   ```bash
   # Windows
   .\check-h2-permissions.ps1
   
   # Linux/Mac
   ./check-h2-permissions.sh
   ```

2. **Deploy:**
   ```bash
   docker compose up -d --build
   ```

3. **Verificar aplicação:**
   - Aplicação: `http://prowebv01.leoproti.com.br:8013`
   - Health check: `http://prowebv01.leoproti.com.br:8013/actuator/health`
   - API REST: `http://prowebv01.leoproti.com.br:8013/produtos`

4. **Monitoramento:**
   ```bash
   # Status do container
   docker compose ps
   
   # Logs em tempo real
   docker compose logs -f app
   
   # Verificar saúde da aplicação
   curl http://localhost:8013/actuator/health
   ```

---

## Fluxo de Deploy Automatizado

- O deploy é feito via GitHub Actions:
  - Ao fazer push na branch `main`, o workflow executa o build, copia os arquivos para o servidor e executa o `docker compose up -d --build`.
  - O banco MariaDB é iniciado antes da aplicação.
  - O deploy é totalmente automatizado, facilitando a entrega contínua.

---

## Histórico do Projeto

- **Início:**  
  Projeto começou com um simples endpoint Hello World para validar o ambiente Spring Boot.
- **CRUD de Produtos:**  
  Implementação completa de cadastro, listagem, edição e exclusão de produtos, com API REST e interface web.
- **CRUD de Alunos:**  
  Expansão do projeto para incluir gerenciamento de alunos, com todos os atributos necessários.
- **Documentação e Deploy:**  
  Adição do Swagger/OpenAPI e automação do deploy com Docker e GitHub Actions.

---

## Observações

- O projeto está pronto para ser usado como base para estudos, testes e demonstrações em sala de aula.
- Para produção, recomenda-se ajustar as configurações de CORS e variáveis sensíveis conforme o ambiente.

---

## Contato

Desenvolvido por Leonardo Vieira Guimarães  
Para dúvidas ou sugestões, entre em contato pelo GitHub.

---

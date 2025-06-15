# Projeto Deploy - Spring Boot CRUD Completo

Este projeto foi desenvolvido para fins didáticos nas disciplinas de Programação Web e Arquitetura de Aplicações Web, demonstrando a construção de APIs REST e aplicações web completas com Spring Boot, integração com banco de dados, documentação automática e deploy automatizado.

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
  H2 em memória para desenvolvimento e MariaDB para produção (via Docker).

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

### Produção (MariaDB via Docker)

1. Execute `docker-compose up -d --build`.
2. A aplicação estará disponível em `http://SEU_DOMINIO:8004` (ou conforme configurado).
3. O banco MariaDB será criado automaticamente com as variáveis definidas no `docker-compose.yml`.

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

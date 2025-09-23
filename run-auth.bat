@echo off
REM 🔐 Script de Configuração de Autenticação JWT
REM Autor: Leonardo Vieira Guimarães
REM Data: 23 de setembro de 2025

echo.
echo ========================================
echo 🔐 SISTEMA DE AUTENTICACAO JWT
echo ========================================
echo.
echo Este script vai configurar o sistema de
echo autenticacao com Spring Security + JWT
echo.

REM Verificar se Maven está instalado
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ ERRO: Maven nao encontrado!
    echo    Instale o Maven primeiro.
    pause
    exit /b 1
)

echo ✅ Maven encontrado!
echo.

REM Limpar e compilar o projeto
echo 🔨 Compilando o projeto...
call mvn clean compile

if %errorlevel% neq 0 (
    echo ❌ ERRO: Falha na compilacao!
    pause
    exit /b 1
)

echo ✅ Compilacao concluida!
echo.

REM Executar o projeto
echo 🚀 Iniciando aplicacao...
echo.
echo ========================================
echo 📋 INFORMACOES IMPORTANTES:
echo ========================================
echo.
echo 🌐 URL da aplicacao: http://localhost:8021
echo 🔍 Console H2: http://localhost:8021/h2-console
echo 📚 Swagger UI: http://localhost:8021/swagger-ui.html
echo.
echo 🔐 USUARIOS DE TESTE:
echo.
echo 👑 ADMIN:
echo    Username: admin
echo    Password: 123456
echo    Roles: ADMIN
echo.
echo 👨‍💼 MANAGER:
echo    Username: manager  
echo    Password: 123456
echo    Roles: MANAGER, USER
echo.
echo 👤 USER:
echo    Username: user
echo    Password: 123456
echo    Roles: USER
echo.
echo 👨‍💻 LEONARDO:
echo    Username: leonardo
echo    Password: 123456
echo    Roles: USER
echo.
echo ========================================
echo 🧪 ENDPOINTS DE TESTE:
echo ========================================
echo.
echo 🔐 LOGIN:
echo    POST /auth/login
echo    Body: {"username": "admin", "password": "123456"}
echo.
echo 📝 REGISTRO:
echo    POST /auth/signup
echo    Body: {
echo      "username": "novo",
echo      "email": "novo@exemplo.com", 
echo      "password": "123456",
echo      "firstName": "Novo",
echo      "lastName": "Usuario",
echo      "roles": ["USER"]
echo    }
echo.
echo 🔄 REFRESH TOKEN:
echo    POST /auth/refresh
echo    Header: Authorization: Bearer {token}
echo.
echo ✅ VERIFICAR TOKEN:
echo    GET /auth/verify
echo    Header: Authorization: Bearer {token}
echo.
echo 👤 PERFIL:
echo    GET /auth/profile
echo    Header: Authorization: Bearer {token}
echo.
echo ========================================
echo 🔧 CONFIGURACOES H2:
echo ========================================
echo.
echo JDBC URL: jdbc:h2:mem:testdb
echo Username: sa
echo Password: (vazio)
echo.
echo ⚠️  Execute o script init-users.sql no H2
echo    para criar os usuarios de teste!
echo.
echo ========================================
echo ▶️  Pressione qualquer tecla para iniciar...
echo ========================================
pause >nul

REM Executar a aplicação
call mvn spring-boot:run

echo.
echo ========================================
echo 🛑 Aplicacao finalizada!
echo ========================================
pause
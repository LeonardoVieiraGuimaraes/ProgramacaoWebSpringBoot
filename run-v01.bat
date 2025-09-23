@echo off
echo 🚀 Iniciando Spring Boot V01 - Versão Simples
echo.
echo 📋 Configurações V01:
echo    🌐 Porta: 8021
echo    🗄️ Banco: H2 (em memória)
echo    🔒 Segurança: Desabilitada
echo    📖 Swagger: http://localhost:8021/swagger-ui/index.html
echo    🗄️ H2 Console: http://localhost:8021/h2-console
echo.
echo ⚠️  JDBC URL para H2: jdbc:h2:mem:testdb
echo ⚠️  Username: sa
echo ⚠️  Password: (vazio)
echo.
mvn spring-boot:run
pause
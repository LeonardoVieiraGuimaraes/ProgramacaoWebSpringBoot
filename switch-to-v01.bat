@echo off
echo 🔄 Alternando para V01 e executando...
echo.

echo 📂 Mudando para branch v01...
git checkout v01

echo.
echo 🚀 Iniciando Spring Boot V01 - Versão Simples
echo.
echo 📋 Configurações V01:
echo    🌐 Porta: 8013
echo    🗄️ Banco: H2 (em memória)
echo    🔒 Segurança: Desabilitada
echo    📖 Swagger: http://localhost:8013/swagger-ui/index.html
echo    🗄️ H2 Console: http://localhost:8013/h2-console
echo.
echo ⚠️  JDBC URL para H2: jdbc:h2:mem:testdb
echo ⚠️  Username: sa
echo ⚠️  Password: (vazio)
echo.
mvn spring-boot:run
pause
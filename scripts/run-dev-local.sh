#!/bin/bash

# 🔧 Script para Executar Desenvolvimento Local (Linux/macOS)
# Branch: v02
# Porta: 8021
# Profile: dev

echo "🔧 Iniciando ambiente de DESENVOLVIMENTO local..."
echo "📍 Branch: v02"
echo "📍 Porta: 8021"
echo "🌐 URL: http://localhost:8021"
echo ""

# Verificar se estamos na branch correta
current_branch=$(git rev-parse --abbrev-ref HEAD)
if [ "$current_branch" != "v02" ]; then
    echo "⚠️  Você não está na branch v02!"
    echo "🔄 Mudando para branch v02..."
    git checkout v02
    git pull origin v02
fi

# Verificar se já está rodando
if lsof -Pi :8021 -sTCP:LISTEN -t >/dev/null ; then
    echo "⚠️  Porta 8021 já está em uso!"
    echo "🔧 Parando processo anterior..."
    pkill -f "server.port=8021" || true
    sleep 2
fi

# Compilar projeto
echo "🔨 Compilando projeto..."
./mvnw clean package -DskipTests -Dspring.profiles.active=dev

# Verificar se compilação foi bem-sucedida
if [ $? -ne 0 ]; then
    echo "❌ Erro na compilação!"
    exit 1
fi

echo ""
echo "🚀 Iniciando aplicação..."
echo "📝 Profile: dev"
echo "📊 Para parar: Ctrl+C"
echo "🌐 Acesse: http://localhost:8021"
echo "🔧 Console H2: http://localhost:8021/h2-console"
echo ""

# Executar aplicação
java -jar target/*.jar \
  --server.port=8021 \
  --spring.profiles.active=dev \
  --spring.datasource.url="jdbc:h2:file:./h2data/devdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL" \
  --spring.datasource.username=sa \
  --spring.datasource.password=devpassword \
  --spring.jpa.hibernate.ddl-auto=update \
  --spring.h2.console.enabled=true \
  --logging.level.com.example.deploy=DEBUG \
  --logging.level.org.springframework.web=DEBUG
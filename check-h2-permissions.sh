#!/bin/bash

# Script para verificar e corrigir permissões H2 antes do deploy
# Uso: ./check-h2-permissions.sh

echo "🔍 Verificando permissões H2..."

# Criar diretório h2data se não existir
if [ ! -d "./h2data" ]; then
    echo "📁 Criando diretório h2data..."
    mkdir -p ./h2data
fi

# Verificar permissões atuais
echo "📊 Permissões atuais:"
ls -la . | grep h2data

# Corrigir permissões
echo "🔧 Corrigindo permissões..."
chmod 755 ./h2data
chown -R $(id -u):$(id -g) ./h2data 2>/dev/null || true

# Verificar se há arquivos H2 existentes
if ls ./h2data/*.db 2>/dev/null; then
    echo "📋 Arquivos H2 encontrados:"
    ls -la ./h2data/
    
    # Corrigir permissões dos arquivos existentes
    chmod 644 ./h2data/*.db 2>/dev/null || true
    chmod 644 ./h2data/*.trace.db 2>/dev/null || true
else
    echo "ℹ️ Nenhum arquivo H2 encontrado (primeiro run)"
fi

# Teste de escrita
echo "✍️ Testando permissões de escrita..."
if touch ./h2data/test-write.tmp; then
    echo "✅ Permissões de escrita OK"
    rm ./h2data/test-write.tmp
else
    echo "❌ Erro nas permissões de escrita!"
    exit 1
fi

# Verificar se Docker está rodando
if ! docker --version > /dev/null 2>&1; then
    echo "❌ Docker não está instalado ou não está rodando!"
    exit 1
fi

echo "✅ Verificação de permissões H2 concluída com sucesso!"

# Opcionalmente, fazer um teste local
read -p "🧪 Deseja fazer um teste local do container? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "🚀 Iniciando teste local..."
    
    # Parar containers existentes
    docker compose down 2>/dev/null || true
    
    # Build e start
    docker compose build --no-cache
    docker compose up -d
    
    echo "⏳ Aguardando inicialização (30s)..."
    sleep 30
    
    # Verificar status
    echo "📊 Status do container:"
    docker compose ps
    
    echo "📝 Logs recentes:"
    docker compose logs --tail=20 app
    
    # Teste de conectividade
    echo "🧪 Testando conectividade..."
    if curl -f http://localhost:8013/actuator/health 2>/dev/null; then
        echo "✅ Aplicação respondendo corretamente!"
    else
        echo "❌ Aplicação não está respondendo"
        echo "📋 Logs completos:"
        docker compose logs app
    fi
    
    # Parar o teste
    docker compose down
fi

echo "🎉 Script concluído!"
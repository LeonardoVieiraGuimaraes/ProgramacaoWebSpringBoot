# Script de diagnóstico para containers que ficam restartando
# Execute este script para identificar o problema

Write-Host "=== Diagnóstico de Container Restart ===" -ForegroundColor Green
Write-Host ""

# Verificar se Docker está rodando
try {
    docker version | Out-Null
    Write-Host "✅ Docker está rodando" -ForegroundColor Green
} catch {
    Write-Host "❌ Docker não está rodando ou não está instalado" -ForegroundColor Red
    exit 1
}

# Verificar containers
Write-Host "📊 Status atual dos containers:" -ForegroundColor Yellow
docker ps -a --filter "name=prowebv01"

Write-Host ""
Write-Host "📋 Logs do container (últimas 50 linhas):" -ForegroundColor Yellow
docker logs prowebv01-app --tail=50 2>$null

if ($LASTEXITCODE -ne 0) {
    Write-Host "⚠️  Container prowebv01-app não encontrado" -ForegroundColor Yellow
    Write-Host "Tentando com docker-compose..." -ForegroundColor Yellow
    
    if (Test-Path "docker-compose.yml") {
        docker-compose logs --tail=50 app 2>$null
    } else {
        Write-Host "❌ Arquivo docker-compose.yml não encontrado" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "🔍 Verificando recursos do sistema:" -ForegroundColor Yellow
docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.MemPerc}}"

Write-Host ""
Write-Host "🧪 Testando conectividade:" -ForegroundColor Yellow

# Testar porta 8013
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8013/" -TimeoutSec 5 -ErrorAction Stop
    Write-Host "✅ Aplicação respondendo na porta 8013" -ForegroundColor Green
} catch {
    Write-Host "❌ Aplicação não responde na porta 8013" -ForegroundColor Red
}

# Testar health endpoint
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8013/actuator/health" -TimeoutSec 5 -ErrorAction Stop
    Write-Host "✅ Health endpoint respondendo" -ForegroundColor Green
    Write-Host "Response: $($response.Content)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Health endpoint não responde" -ForegroundColor Red
}

Write-Host ""
Write-Host "🔧 Verificando configurações:" -ForegroundColor Yellow

# Verificar docker-compose.yml
if (Test-Path "docker-compose.yml") {
    Write-Host "✅ docker-compose.yml encontrado" -ForegroundColor Green
    Write-Host "📋 Configuração atual:" -ForegroundColor Cyan
    Get-Content "docker-compose.yml" | Select-Object -First 20
} else {
    Write-Host "❌ docker-compose.yml não encontrado" -ForegroundColor Red
}

Write-Host ""
Write-Host "📁 Verificando arquivos necessários:" -ForegroundColor Yellow

$arquivos = @(
    "target\*.jar",
    "Dockerfile.spring",
    "src\main\resources\application.yaml"
)

foreach ($arquivo in $arquivos) {
    if (Test-Path $arquivo) {
        Write-Host "✅ $arquivo existe" -ForegroundColor Green
    } else {
        Write-Host "❌ $arquivo não encontrado" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "🏥 Comandos úteis para debug:" -ForegroundColor Magenta
Write-Host "1. Ver logs em tempo real: docker-compose logs -f app" -ForegroundColor Gray
Write-Host "2. Entrar no container: docker exec -it prowebv01-app /bin/bash" -ForegroundColor Gray
Write-Host "3. Rebuild completo: docker-compose down && docker-compose build --no-cache && docker-compose up -d" -ForegroundColor Gray
Write-Host "4. Ver processos Java: docker exec prowebv01-app ps aux | grep java" -ForegroundColor Gray

Write-Host ""
Write-Host "🔍 Possíveis causas de restart:" -ForegroundColor Red
Write-Host "- Porta 8013 já em uso por outro processo" -ForegroundColor Yellow
Write-Host "- Erro no arquivo JAR (build falhou)" -ForegroundColor Yellow
Write-Host "- Problemas de memória (OOM)" -ForegroundColor Yellow
Write-Host "- Configuração incorreta no application.yaml" -ForegroundColor Yellow
Write-Host "- Health check falhando" -ForegroundColor Yellow

Write-Host ""
Write-Host "Pressione Enter para continuar..."
Read-Host
# Script PowerShell para verificar e corrigir permissões H2 antes do deploy
# Uso: .\check-h2-permissions.ps1

Write-Host "🔍 Verificando permissões H2..." -ForegroundColor Cyan

# Criar diretório h2data se não existir
if (-not (Test-Path "./h2data")) {
    Write-Host "📁 Criando diretório h2data..." -ForegroundColor Yellow
    New-Item -ItemType Directory -Path "./h2data" -Force | Out-Null
}

# Verificar permissões atuais
Write-Host "📊 Verificando diretório h2data:" -ForegroundColor Cyan
Get-ChildItem -Path "." -Name "h2data" | ForEach-Object { 
    Get-Item $_ | Select-Object Name, Mode, LastWriteTime 
}

# Verificar se há arquivos H2 existentes
$h2Files = Get-ChildItem -Path "./h2data" -Filter "*.db" -ErrorAction SilentlyContinue
if ($h2Files) {
    Write-Host "📋 Arquivos H2 encontrados:" -ForegroundColor Green
    Get-ChildItem -Path "./h2data" | Select-Object Name, Length, LastWriteTime
} else {
    Write-Host "ℹ️ Nenhum arquivo H2 encontrado (primeiro run)" -ForegroundColor Blue
}

# Teste de escrita
Write-Host "✍️ Testando permissões de escrita..." -ForegroundColor Yellow
try {
    $testFile = "./h2data/test-write.tmp"
    "test" | Out-File -FilePath $testFile -Encoding utf8
    Remove-Item $testFile -ErrorAction SilentlyContinue
    Write-Host "✅ Permissões de escrita OK" -ForegroundColor Green
} catch {
    Write-Host "❌ Erro nas permissões de escrita!" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}

# Verificar se Docker está rodando
try {
    $dockerVersion = docker --version 2>$null
    if ($dockerVersion) {
        Write-Host "✅ Docker está disponível: $dockerVersion" -ForegroundColor Green
    } else {
        throw "Docker não encontrado"
    }
} catch {
    Write-Host "❌ Docker não está instalado ou não está rodando!" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Verificação de permissões H2 concluída com sucesso!" -ForegroundColor Green

# Opcionalmente, fazer um teste local
$response = Read-Host "🧪 Deseja fazer um teste local do container? (y/n)"
if ($response -eq "y" -or $response -eq "Y") {
    Write-Host "🚀 Iniciando teste local..." -ForegroundColor Cyan
    
    # Parar containers existentes
    Write-Host "🛑 Parando containers existentes..." -ForegroundColor Yellow
    docker compose down 2>$null | Out-Null
    
    # Build e start
    Write-Host "🔨 Fazendo build do container..." -ForegroundColor Yellow
    docker compose build --no-cache
    
    Write-Host "🚀 Iniciando container..." -ForegroundColor Yellow
    docker compose up -d
    
    Write-Host "⏳ Aguardando inicialização (30s)..." -ForegroundColor Yellow
    Start-Sleep -Seconds 30
    
    # Verificar status
    Write-Host "📊 Status do container:" -ForegroundColor Cyan
    docker compose ps
    
    Write-Host "📝 Logs recentes:" -ForegroundColor Cyan
    docker compose logs --tail=20 app
    
    # Teste de conectividade
    Write-Host "🧪 Testando conectividade..." -ForegroundColor Yellow
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8013/actuator/health" -UseBasicParsing -TimeoutSec 10
        if ($response.StatusCode -eq 200) {
            Write-Host "✅ Aplicação respondendo corretamente!" -ForegroundColor Green
        } else {
            Write-Host "❌ Aplicação retornou status: $($response.StatusCode)" -ForegroundColor Red
        }
    } catch {
        Write-Host "❌ Aplicação não está respondendo" -ForegroundColor Red
        Write-Host "📋 Logs completos:" -ForegroundColor Yellow
        docker compose logs app
    }
    
    # Parar o teste
    Write-Host "🛑 Parando teste..." -ForegroundColor Yellow
    docker compose down
}

Write-Host "🎉 Script concluído!" -ForegroundColor Green
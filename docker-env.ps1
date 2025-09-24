# 🚀 ProWeb Docker Environment Manager (PowerShell)
# Gerenciador de ambientes Docker para o projeto ProWeb

param(
    [Parameter(Mandatory=$true)]
    [string]$Environment,
    
    [Parameter(Mandatory=$true)]
    [string]$Action
)

# Função para exibir ajuda
function Show-Help {
    Write-Host "🚀 ProWeb Docker Environment Manager" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Uso: .\docker-env.ps1 [AMBIENTE] [AÇÃO]" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Ambientes disponíveis:" -ForegroundColor Yellow
    Write-Host "  dev       - Desenvolvimento (porta 8013)"
    Write-Host "  staging   - Homologação (porta 8020)"
    Write-Host "  prod      - Produção (porta 8021)"
    Write-Host ""
    Write-Host "Ações disponíveis:" -ForegroundColor Yellow
    Write-Host "  up        - Iniciar ambiente"
    Write-Host "  down      - Parar ambiente"
    Write-Host "  logs      - Ver logs"
    Write-Host "  status    - Status dos containers"
    Write-Host "  restart   - Reiniciar ambiente"
    Write-Host "  build     - Rebuild completo"
    Write-Host "  config    - Verificar configuração"
    Write-Host "  clean     - Limpeza completa"
    Write-Host ""
    Write-Host "Exemplos:" -ForegroundColor Yellow
    Write-Host "  .\docker-env.ps1 dev up          # Iniciar desenvolvimento"
    Write-Host "  .\docker-env.ps1 staging logs    # Ver logs do staging"
    Write-Host "  .\docker-env.ps1 prod status     # Status da produção"
    Write-Host ""
}

# Função para determinar arquivo compose
function Get-ComposeFile {
    param([string]$env)
    switch ($env) {
        "dev" { return "docker-compose.yml" }
        "staging" { return "docker-compose-staging.yml" }
        "prod" { return "docker-compose-prod.yml" }
        default { 
            Write-Host "❌ Ambiente inválido: $env" -ForegroundColor Red
            Show-Help
            exit 1
        }
    }
}

# Função para obter porta do ambiente
function Get-Port {
    param([string]$env)
    switch ($env) {
        "dev" { return "8013" }
        "staging" { return "8020" }
        "prod" { return "8021" }
    }
}

# Função para obter URL do ambiente
function Get-Url {
    param([string]$env)
    switch ($env) {
        "dev" { return "http://localhost:8013" }
        "staging" { return "https://staging.proweb.leoproti.com.br" }
        "prod" { return "https://proweb.leoproti.com.br" }
    }
}

# Obter configurações do ambiente
$ComposeFile = Get-ComposeFile $Environment
$Port = Get-Port $Environment
$Url = Get-Url $Environment

# Verificar se arquivo compose existe
if (-not (Test-Path $ComposeFile)) {
    Write-Host "❌ Arquivo $ComposeFile não encontrado!" -ForegroundColor Red
    exit 1
}

Write-Host "🎯 Ambiente: $Environment" -ForegroundColor Cyan
Write-Host "📄 Compose: $ComposeFile" -ForegroundColor Cyan
Write-Host "🔌 Porta: $Port" -ForegroundColor Cyan
Write-Host "🌐 URL: $Url" -ForegroundColor Cyan
Write-Host ""

# Executar ação
switch ($Action) {
    "up" {
        Write-Host "🚀 Iniciando ambiente $Environment..." -ForegroundColor Green
        docker-compose -f $ComposeFile up -d
        Write-Host ""
        Write-Host "✅ Ambiente iniciado com sucesso!" -ForegroundColor Green
        Write-Host "🌐 Acesse: $Url" -ForegroundColor Cyan
        Write-Host "❤️  Health: $Url/actuator/health" -ForegroundColor Cyan
    }
    
    "down" {
        Write-Host "⏹️  Parando ambiente $Environment..." -ForegroundColor Yellow
        docker-compose -f $ComposeFile down --remove-orphans
        Write-Host "✅ Ambiente parado!" -ForegroundColor Green
    }
    
    "logs" {
        Write-Host "📋 Logs do ambiente ${Environment}:" -ForegroundColor Blue
        docker-compose -f $ComposeFile logs -f --tail=100
    }
    
    "status" {
        Write-Host "📊 Status do ambiente ${Environment}:" -ForegroundColor Blue
        docker-compose -f $ComposeFile ps
        Write-Host ""
        Write-Host "🔍 Verificando conectividade..." -ForegroundColor Cyan
        try {
            Invoke-WebRequest -Uri "$Url/actuator/health" -TimeoutSec 5 -ErrorAction Stop | Out-Null
            Write-Host "✅ Aplicação está respondendo!" -ForegroundColor Green
        }
        catch {
            Write-Host "❌ Aplicação não está respondendo" -ForegroundColor Red
        }
    }
    
    "restart" {
        Write-Host "🔄 Reiniciando ambiente $Environment..." -ForegroundColor Yellow
        docker-compose -f $ComposeFile restart
        Write-Host "✅ Ambiente reiniciado!" -ForegroundColor Green
    }
    
    "build" {
        Write-Host "🔨 Rebuild completo do ambiente $Environment..." -ForegroundColor Magenta
        docker-compose -f $ComposeFile down --remove-orphans
        docker-compose -f $ComposeFile build --no-cache
        docker-compose -f $ComposeFile up -d
        Write-Host "✅ Rebuild concluído!" -ForegroundColor Green
        Write-Host "🌐 Acesse: $Url" -ForegroundColor Cyan
    }
    
    "config" {
        Write-Host "⚙️  Configuração do ambiente ${Environment}:" -ForegroundColor Blue
        docker-compose -f $ComposeFile config
    }
    
    "clean" {
        Write-Host "🧹 Limpeza completa do ambiente $Environment..." -ForegroundColor Red
        Write-Host "⚠️  Isso irá remover containers, imagens e volumes!" -ForegroundColor Yellow
        $confirmation = Read-Host "Deseja continuar? (y/N)"
        if ($confirmation -eq 'y' -or $confirmation -eq 'Y') {
            docker-compose -f $ComposeFile down --remove-orphans --volumes --rmi all
            Write-Host "✅ Limpeza concluída!" -ForegroundColor Green
        } else {
            Write-Host "ℹ️  Operação cancelada" -ForegroundColor Blue
        }
    }
    
    default {
        Write-Host "❌ Ação inválida: $Action" -ForegroundColor Red
        Show-Help
        exit 1
    }
}
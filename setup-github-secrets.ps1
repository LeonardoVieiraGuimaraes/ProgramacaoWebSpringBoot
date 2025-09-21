# Script para verificar e configurar GitHub Secrets
# PowerShell - Execute no diretório do projeto

Write-Host "=== Verificação de Configuração GitHub Secrets ===" -ForegroundColor Green
Write-Host ""

# Verificar se estamos em um repositório Git
if (!(Test-Path ".git")) {
    Write-Host "❌ Erro: Este diretório não é um repositório Git!" -ForegroundColor Red
    exit 1
}

# Obter informações do repositório
$remoteUrl = git remote get-url origin 2>$null
if ($remoteUrl) {
    Write-Host "📁 Repositório: $remoteUrl" -ForegroundColor Blue
    
    # Extrair owner e repo do URL
    if ($remoteUrl -match "github\.com[:/]([^/]+)/([^/.]+)") {
        $owner = $matches[1]
        $repo = $matches[2]
        
        Write-Host "👤 Owner: $owner" -ForegroundColor Cyan
        Write-Host "📂 Repository: $repo" -ForegroundColor Cyan
        
        $settingsUrl = "https://github.com/$owner/$repo/settings/secrets/actions"
        Write-Host ""
        Write-Host "🔗 Link direto para configurar Secrets:" -ForegroundColor Yellow
        Write-Host $settingsUrl -ForegroundColor White
        
        # Tentar abrir no navegador
        try {
            Start-Process $settingsUrl
            Write-Host "✅ Abrindo navegador..." -ForegroundColor Green
        } catch {
            Write-Host "⚠️  Copie e cole o link acima no seu navegador" -ForegroundColor Yellow
        }
    }
} else {
    Write-Host "❌ Não foi possível obter informações do repositório remoto" -ForegroundColor Red
}

Write-Host ""
Write-Host "📋 Secrets necessários para este projeto:" -ForegroundColor Magenta
Write-Host ""
Write-Host "1. HOME_SERVER_SSH_KEY" -ForegroundColor White
Write-Host "   └── Sua chave SSH privada para acesso ao servidor" -ForegroundColor Gray
Write-Host ""

Write-Host "🔑 Como gerar/obter sua chave SSH:" -ForegroundColor Yellow
Write-Host "1. No terminal: ssh-keygen -t ed25519 -C 'seu-email@exemplo.com'" -ForegroundColor Gray
Write-Host "2. Copie a chave PRIVADA: Get-Content ~/.ssh/id_ed25519" -ForegroundColor Gray
Write-Host "3. Cole no GitHub Secret (incluindo BEGIN/END)" -ForegroundColor Gray
Write-Host ""

Write-Host "⚠️  IMPORTANTE:" -ForegroundColor Red
Write-Host "- Cole a chave PRIVADA completa (não a .pub)" -ForegroundColor Red
Write-Host "- Inclua as linhas BEGIN/END" -ForegroundColor Red
Write-Host "- Não adicione espaços extras" -ForegroundColor Red
Write-Host ""

Write-Host "🧪 Para testar se funcionou:" -ForegroundColor Green
Write-Host "1. Faça um commit e push" -ForegroundColor Gray
Write-Host "2. Vá na aba Actions do GitHub" -ForegroundColor Gray
Write-Host "3. Verifique se o workflow executa sem erros" -ForegroundColor Gray

Write-Host ""
Write-Host "Pressione Enter para continuar..."
Read-Host
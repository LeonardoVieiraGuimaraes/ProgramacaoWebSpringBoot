# 🔧 Script para Executar Desenvolvimento Local (Windows)
# Branch: v02
# Porta: 8021
# Profile: dev

Write-Host "🔧 Iniciando ambiente de DESENVOLVIMENTO local..." -ForegroundColor Blue
Write-Host "📍 Branch: v02" -ForegroundColor Cyan
Write-Host "📍 Porta: 8021" -ForegroundColor Cyan
Write-Host "🌐 URL: http://localhost:8021" -ForegroundColor Green
Write-Host ""

# Verificar se estamos na branch correta
$currentBranch = git rev-parse --abbrev-ref HEAD
if ($currentBranch -ne "v02") {
    Write-Host "⚠️  Você não está na branch v02!" -ForegroundColor Yellow
    Write-Host "🔄 Mudando para branch v02..." -ForegroundColor Blue
    git checkout v02
    git pull origin v02
}

# Verificar se já está rodando
$process = Get-NetTCPConnection -LocalPort 8021 -ErrorAction SilentlyContinue
if ($process) {
    Write-Host "⚠️  Porta 8021 já está em uso!" -ForegroundColor Yellow
    Write-Host "🔧 Parando processo anterior..." -ForegroundColor Blue
    Get-Process -Name "java" -ErrorAction SilentlyContinue | Where-Object {$_.CommandLine -like "*server.port=8021*"} | Stop-Process -Force
    Start-Sleep -Seconds 2
}

# Compilar projeto
Write-Host "🔨 Compilando projeto..." -ForegroundColor Blue
& ./mvnw.cmd clean package -DskipTests -Dspring.profiles.active=dev

# Verificar se compilação foi bem-sucedida
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Erro na compilação!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "🚀 Iniciando aplicação..." -ForegroundColor Green
Write-Host "📝 Profile: dev" -ForegroundColor Cyan
Write-Host "📊 Para parar: Ctrl+C" -ForegroundColor Yellow
Write-Host "🌐 Acesse: http://localhost:8021" -ForegroundColor Green
Write-Host "🔧 Console H2: http://localhost:8021/h2-console" -ForegroundColor Cyan
Write-Host ""

# Executar aplicação
$jarFile = Get-ChildItem -Path "./target" -Name "*.jar" | Select-Object -First 1
if ($jarFile) {
    java -jar "target/$jarFile" `
      --server.port=8021 `
      --spring.profiles.active=dev `
      --spring.datasource.url="jdbc:h2:file:./h2data/devdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL" `
      --spring.datasource.username=sa `
      --spring.datasource.password=devpassword `
      --spring.jpa.hibernate.ddl-auto=update `
      --spring.h2.console.enabled=true `
      --logging.level.com.example.deploy=DEBUG `
      --logging.level.org.springframework.web=DEBUG
} else {
    Write-Host "❌ Arquivo JAR não encontrado!" -ForegroundColor Red
}
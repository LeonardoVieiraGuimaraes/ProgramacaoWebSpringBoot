# 🚀 Guia de Deploy Multi-Ambiente

## 📋 Visão Geral

Este projeto possui dois ambientes de deploy automatizados:

### 🏭 **Produção (v01)**
- **Branch:** `main`
- **URL:** https://prowebv01.leoproti.com.br
- **Porta:** 8013
- **Container:** `prowebv01-app`
- **Uso:** Versão estável para usuários finais

### 🧪 **Staging (v02)**
- **Branch:** `v02`
- **URL:** https://prowebv02.leoproti.com.br
- **Porta:** 8014
- **Container:** `prowebv02-app`
- **Uso:** Testes e desenvolvimento de novas funcionalidades

## 🔄 Como Funciona o Deploy

### Deploy Automático por Branch:

```bash
# Deploy para PRODUÇÃO
git checkout main
git push origin main
# → Deploya automaticamente em prowebv01.leoproti.com.br

# Deploy para STAGING
git checkout v02
git push origin v02
# → Deploya automaticamente em prowebv02.leoproti.com.br
```

### Deploy Manual:

1. Vá para **Actions** no GitHub
2. Selecione **Deploy Spring Boot App - Multi Environment**
3. Clique em **Run workflow**
4. Escolha o ambiente:
   - `auto` - Detecta automaticamente pela branch
   - `production` - Força deploy em produção
   - `staging` - Força deploy em staging

## 🌊 Fluxo de Desenvolvimento Recomendado

### 1. Desenvolvimento de Nova Feature:
```bash
# Trabalhar na branch v02
git checkout v02
git pull origin v02

# Fazer mudanças
# ... código ...

git add .
git commit -m "✨ Nova funcionalidade X"
git push origin v02
# → Deploy automático para STAGING
```

### 2. Teste em Staging:
- Acesse: https://prowebv02.leoproti.com.br
- Teste todas as funcionalidades
- Verifique se não há regressões

### 3. Promover para Produção:
```bash
# Quando estiver tudo ok, promover para main
git checkout main
git pull origin main
git merge v02
git push origin main
# → Deploy automático para PRODUÇÃO

# Criar tag da nova versão
git tag v02
git push origin v02
```

## 🏗️ Configuração dos Containers

### Diferenças entre Ambientes:

| Aspecto | Produção (v01) | Staging (v02) |
|---------|----------------|---------------|
| Porta | 8013 | 8014 |
| Container | `prowebv01-app` | `prowebv02-app` |
| Network | `prowebv01-network` | `prowebv02-network` |
| Dados H2 | `/projetos/prowebv01/h2data` | `/projetos/prowebv02/h2data` |
| Logs | `prowebv01-*.log` | `prowebv02-*.log` |

## 🛠️ Comandos Úteis

### Verificar Status dos Containers:
```bash
# No servidor
docker ps | grep proweb

# Logs da produção
docker logs prowebv01-app

# Logs do staging
docker logs prowebv02-app
```

### Reiniciar Ambiente Específico:
```bash
# Reiniciar produção
cd /home/leonardovieiraxy/projetos/prowebv01
docker-compose restart

# Reiniciar staging
cd /home/leonardovieiraxy/projetos/prowebv02
docker-compose restart
```

## 🔍 Monitoramento

### Health Checks:
- **Produção:** https://prowebv01.leoproti.com.br/actuator/health
- **Staging:** https://prowebv02.leoproti.com.br/actuator/health

### APIs:
- **Produção:** https://prowebv01.leoproti.com.br/produtos
- **Staging:** https://prowebv02.leoproti.com.br/produtos

## 🚨 Troubleshooting

### Container não inicia:
```bash
# Verificar logs
docker logs prowebv02-app

# Verificar rede
docker network ls | grep proweb

# Verificar portas
ss -tulpn | grep :8014
```

### Problemas de permissão H2:
```bash
# Corrigir permissões
sudo chown -R leonardovieiraxy:leonardovieiraxy /home/leonardovieiraxy/projetos/prowebv02/h2data
chmod 755 /home/leonardovieiraxy/projetos/prowebv02/h2data
```

### Deploy falhou:
1. Verifique os logs do GitHub Actions
2. Verifique se o SSH está funcionando
3. Verifique se há conflitos de porta
4. Reinicie o deploy manualmente

## 📝 Notas Importantes

- ✅ Cada ambiente tem seu próprio banco H2
- ✅ Logs são separados por ambiente
- ✅ Rollback é simples (revert + push)
- ✅ Zero downtime entre v01 e v02
- ⚠️ Sempre teste em staging antes de produção
- ⚠️ Mantenha as branches sincronizadas
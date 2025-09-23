# 🔄 Workflows do Projeto

## 📋 Workflows Ativos

### 🚀 **MultiEnvDeploy.yml** (Principal)
- **Status:** ✅ ATIVO
- **Função:** Deploy multi-ambiente inteligente
- **Triggers:**
  - Push na branch `main` → Deploy para produção
  - Push na branch `v02` → Deploy para staging
  - Manual → Escolha do ambiente

**Configuração:**
```yaml
Produção:  prowebv01.leoproti.com.br:8013
Staging:   prowebv02.leoproti.com.br:8014
```

## 📝 Workflows Desabilitados

### 🗄️ **HomeServer-DISABLED.yml** (Antigo)
- **Status:** ❌ DESABILITADO
- **Motivo:** Substituído pelo MultiEnvDeploy.yml
- **Função Original:** Deploy apenas para produção
- **Preservado:** Para referência histórica

## 🎯 Como Usar

### Deploy Automático:
```bash
# Deploy para STAGING (v02)
git checkout v02
git push origin v02

# Deploy para PRODUÇÃO (v01)
git checkout main  
git push origin main
```

### Deploy Manual:
1. Vá para **GitHub Actions**
2. Selecione **Deploy Spring Boot App - Multi Environment**
3. Clique **Run workflow**
4. Escolha o ambiente

## 🔧 Troubleshooting

### Se dois workflows executarem:
- Verifique se `HomeServer-DISABLED.yml` está comentado
- Apenas `MultiEnvDeploy.yml` deve estar ativo

### Para reativar o workflow antigo:
1. Descomente as linhas `on:` em `HomeServer-DISABLED.yml`
2. Renomeie para `.yml`
3. Desabilite `MultiEnvDeploy.yml`

## 📊 Comparação

| Aspecto | HomeServer (Antigo) | MultiEnvDeploy (Novo) |
|---------|---------------------|----------------------|
| Ambientes | 1 (apenas produção) | 2 (produção + staging) |
| Branches | Apenas `main` | `main` + `v02` |
| Flexibilidade | Limitada | Alta |
| Deploy Manual | ❌ | ✅ |
| Isolamento | ❌ | ✅ |

## ✅ Recomendação

**Use apenas o `MultiEnvDeploy.yml`** - ele faz tudo que o antigo fazia, mas melhor e com mais recursos!
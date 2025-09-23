# Histórico de Versões

## v02 (Em Desenvolvimento)
- **Data de Início:** 23 de setembro de 2025
- **Status:** Em desenvolvimento
- **Branch:** v02
- **Deploy:** Staging environment
- **URL:** https://prowebv02.leoproti.com.br
- **Porta:** 8014

### Mudanças Planejadas:
- [ ] Melhorias na API REST
- [ ] Novos recursos
- [ ] Otimizações de performance
- [ ] Correções de bugs

### 🚀 Estratégia de Deploy Multi-Ambiente:

#### 📋 Ambientes Configurados:
1. **Produção (v01):**
   - Branch: `main`
   - URL: https://prowebv01.leoproti.com.br
   - Porta: 8013
   - Container: `prowebv01-app`
   - Status: Estável

2. **Staging (v02):**
   - Branch: `v02`
   - URL: https://prowebv02.leoproti.com.br
   - Porta: 8014
   - Container: `prowebv02-app`
   - Status: Desenvolvimento

#### 🔄 Workflow de Deploy:
- **Push para `main`** → Deploy automático para Produção
- **Push para `v02`** → Deploy automático para Staging
- **Deploy manual** → Escolha do ambiente via GitHub Actions

---

## v01 (Produção)
- **Data de Release:** 23 de setembro de 2025
- **Status:** Estável em produção
- **Tag:** v01
- **URL:** https://prowebv01.leoproti.com.br

### Principais Recursos:
- ✅ API REST para Produtos e Alunos
- ✅ Interface web com Bootstrap
- ✅ Configuração CORS corrigida
- ✅ Docker containerizado
- ✅ Deploy via Cloudflare
- ✅ Banco de dados H2
- ✅ Health checks configurados

### Correções Implementadas:
- ✅ Erro CORS resolvido (allowedOriginPatterns)
- ✅ Remoção de @CrossOrigin conflitantes
- ✅ Configuração WebConfig otimizada
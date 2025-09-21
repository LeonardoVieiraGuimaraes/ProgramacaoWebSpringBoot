# 🔐 Configuração GitHub Secrets

Este documento explica como configurar os secrets necessários para o GitHub Actions funcionar corretamente.

## 🚨 Problema Comum

**O GitHub Actions NÃO consegue criar secrets automaticamente!** 
Os secrets devem ser configurados manualmente na interface web do GitHub.

## 📝 Passo a Passo Detalhado

### 1. Acesse seu repositório no GitHub
```
https://github.com/LeonardoVieiraGuimaraes/ProgramacaoWebSpringBoot
```

### 2. Navegue até as configurações
- Clique na aba **"Settings"** (último item do menu)
- No menu lateral esquerdo, procure por **"Secrets and variables"**
- Clique em **"Actions"**

### 3. Adicione o secret necessário
- Clique no botão **"New repository secret"**
- Preencha os campos:

#### Nome do Secret:
```
HOME_SERVER_SSH_KEY
```

#### Valor do Secret:
Sua chave SSH privada completa, incluindo as linhas de cabeçalho e rodapé:
```
-----BEGIN OPENSSH PRIVATE KEY-----
b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAA...
(seu conteúdo da chave aqui)
...AAAAB3NzaC1yc2EAAAADAQABAAABgQC...
-----END OPENSSH PRIVATE KEY-----
```

### 4. Como obter sua chave SSH

#### No Windows (PowerShell):
```powershell
# Gerar nova chave (se não tiver)
ssh-keygen -t ed25519 -C "seu-email@example.com"

# Visualizar chave privada
Get-Content ~/.ssh/id_ed25519
```

#### No Linux/macOS:
```bash
# Gerar nova chave (se não tiver)
ssh-keygen -t ed25519 -C "seu-email@example.com"

# Visualizar chave privada
cat ~/.ssh/id_ed25519
```

### 5. Configuração no servidor (uma vez só)
Adicione a chave PÚBLICA no servidor:
```bash
# Visualizar chave pública
cat ~/.ssh/id_ed25519.pub

# No servidor, adicione ao authorized_keys
echo "sua-chave-publica-aqui" >> ~/.ssh/authorized_keys
```

## ✅ Verificação

Após configurar o secret:

1. **Execute o script de verificação:**
   ```powershell
   .\setup-github-secrets.ps1
   ```

2. **Faça um commit e push:**
   ```bash
   git add .
   git commit -m "test: verificar secrets configurados"
   git push
   ```

3. **Verifique na aba Actions:**
   - Vá em: https://github.com/LeonardoVieiraGuimaraes/ProgramacaoWebSpringBoot/actions
   - Veja se o workflow executa sem erros de autenticação

## 🔧 Troubleshooting

### Erro: "HOME_SERVER_SSH_KEY secret is not configured"
- ✅ Verifique se o nome está exato: `HOME_SERVER_SSH_KEY`
- ✅ Certifique-se que configurou no repositório correto
- ✅ Aguarde alguns minutos após configurar

### Erro: "Permission denied (publickey)"
- ✅ Verifique se adicionou a chave PÚBLICA no servidor
- ✅ Teste a conexão SSH manualmente
- ✅ Certifique-se que copiou a chave PRIVADA completa

### Erro: "Invalid format"
- ✅ Inclua as linhas `-----BEGIN` e `-----END`
- ✅ Não adicione espaços ou quebras de linha extras
- ✅ Copie direto do arquivo, não reescreva

## 📞 Link Rápido

**Configurar Secrets Agora:**
https://github.com/LeonardoVieiraGuimaraes/ProgramacaoWebSpringBoot/settings/secrets/actions

---

💡 **Lembre-se:** Esta é uma configuração única! Uma vez configurado, funcionará para todos os pushes futuros.
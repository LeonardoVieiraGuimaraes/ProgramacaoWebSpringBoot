# 🛠️ Troubleshooting H2 Database - ProWebV01

## 📋 Problemas Comuns e Soluções

### 🚨 AccessDeniedException - Problemas de Permissão

**Sintomas:**
- Container reinicia constantemente
- Logs mostram `java.nio.file.AccessDeniedException`
- Erro ao acessar `/data/h2db/db.mv.db`

**Soluções:**

#### 1. Script Automático (Recomendado)
```bash
# Linux/Mac
./check-h2-permissions.sh

# Windows PowerShell
.\check-h2-permissions.ps1
```

#### 2. Correção Manual Local
```bash
# Parar containers
docker compose down

# Remover diretório problemático
rm -rf ./h2data

# Criar com permissões corretas
mkdir -p ./h2data
chmod 755 ./h2data
chown -R $(id -u):$(id -g) ./h2data

# Reiniciar
docker compose up -d
```

#### 3. Correção Manual no Servidor
```bash
cd /home/leonardovieiraxy/projetos/prowebv01

# Parar containers
docker compose down

# Corrigir permissões
sudo rm -rf ./h2data
mkdir -p ./h2data
chmod 755 ./h2data
sudo chown -R leonardovieiraxy:leonardovieiraxy ./h2data

# Verificar permissões
ls -la ./h2data

# Reiniciar
docker compose up -d
```

### 🔍 Verificação de Status

#### 1. Status do Container
```bash
docker compose ps -a
docker compose logs --tail=50 app
```

#### 2. Verificar Permissões
```bash
ls -la ./h2data/
```

#### 3. Teste de Conectividade
```bash
curl http://localhost:8013/actuator/health
curl http://localhost:8013/
```

### 📊 Logs Importantes

#### Container Logs
```bash
# Logs recentes
docker compose logs --tail=100 app

# Logs em tempo real
docker compose logs -f app

# Logs específicos de erro
docker compose logs app | grep -i error
docker compose logs app | grep -i exception
```

#### Sistema de Arquivos
```bash
# Verificar espaço em disco
df -h

# Verificar inodes
df -i

# Verificar permissões detalhadas
namei -l ./h2data/
```

### 🚀 Deploy e GitHub Actions

O workflow do GitHub Actions agora inclui verificações automáticas:

1. **Limpeza automática** do diretório h2data problemático
2. **Criação com permissões corretas** (755)
3. **Definição do proprietário** correto
4. **Verificação de status** após deploy
5. **Detecção automática** de problemas de permissão
6. **Correção automática** com sudo quando necessário

### 🔧 Configurações Docker

#### docker-compose.yml
```yaml
services:
  app:
    user: "1000:1000"  # Usuário específico
    volumes:
      - ./h2data:/data/h2db:rw  # Modo read-write explícito
    environment:
      - SPRING_DATASOURCE_URL=jdbc:h2:file:/data/h2db/db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL;WRITE_DELAY=0;LOCK_TIMEOUT=10000
```

#### Dockerfile.spring
```dockerfile
# Usuário específico com UID 1000
RUN groupadd -r spring && useradd -r -g spring -u 1000 spring
USER spring:spring
```

### 🚨 Sinais de Alerta

#### Container Reiniciando
- Verificar logs para `AccessDeniedException`
- Verificar permissões do diretório h2data
- Verificar se o usuário do container tem acesso

#### Aplicação Não Responde
- Verificar se a porta 8013 está disponível
- Verificar health endpoint: `/actuator/health`
- Verificar logs de inicialização do Spring Boot

#### Banco de Dados Corrompido
```bash
# Backup dos dados (se possível)
cp -r ./h2data ./h2data.backup.$(date +%Y%m%d_%H%M%S)

# Reset completo do banco
rm -rf ./h2data
mkdir -p ./h2data
chmod 755 ./h2data

# Reiniciar aplicação
docker compose down && docker compose up -d
```

### 📞 Debug Avançado

#### Entrar no Container
```bash
# Acessar shell do container
docker exec -it prowebv01-app /bin/sh

# Verificar permissões dentro do container
ls -la /data/h2db/

# Verificar usuário atual
id

# Tentar criar arquivo de teste
touch /data/h2db/test.txt
```

#### Verificar Network
```bash
# Verificar portas
netstat -tulpn | grep 8013
ss -tulpn | grep 8013

# Verificar conectividade
telnet localhost 8013
nc -zv localhost 8013
```

### 📝 Checklist de Deploy

- [ ] Scripts de verificação executados localmente
- [ ] Permissões h2data corretas (755)
- [ ] Container build sem erros
- [ ] Container inicia sem restart loops
- [ ] Health endpoint responde
- [ ] Aplicação principal responde
- [ ] Logs não mostram erros de permissão
- [ ] GitHub Actions completa com sucesso

### 🆘 Contatos e Recursos

- **Documentação Spring Boot:** https://spring.io/projects/spring-boot
- **H2 Database:** https://www.h2database.com/
- **Docker Compose:** https://docs.docker.com/compose/
- **GitHub Actions:** https://docs.github.com/en/actions

---

**Última atualização:** $(date)
**Versão do projeto:** ProWebV01 - Spring Boot 3.4.5
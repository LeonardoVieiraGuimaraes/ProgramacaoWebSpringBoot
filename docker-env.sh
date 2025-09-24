#!/bin/bash

# 🚀 ProWeb Docker Environment Manager
# Gerenciador de ambientes Docker para o projeto ProWeb

set -e

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Função para exibir ajuda
show_help() {
    echo -e "${CYAN}🚀 ProWeb Docker Environment Manager${NC}"
    echo ""
    echo "Uso: $0 [AMBIENTE] [AÇÃO]"
    echo ""
    echo -e "${YELLOW}Ambientes disponíveis:${NC}"
    echo "  dev       - Desenvolvimento (porta 8013)"
    echo "  staging   - Homologação (porta 8020)"
    echo "  prod      - Produção (porta 8021)"
    echo ""
    echo -e "${YELLOW}Ações disponíveis:${NC}"
    echo "  up        - Iniciar ambiente"
    echo "  down      - Parar ambiente"
    echo "  logs      - Ver logs"
    echo "  status    - Status dos containers"
    echo "  restart   - Reiniciar ambiente"
    echo "  build     - Rebuild completo"
    echo "  config    - Verificar configuração"
    echo "  clean     - Limpeza completa"
    echo ""
    echo -e "${YELLOW}Exemplos:${NC}"
    echo "  $0 dev up          # Iniciar desenvolvimento"
    echo "  $0 staging logs    # Ver logs do staging"
    echo "  $0 prod status     # Status da produção"
    echo ""
}

# Função para determinar arquivo compose
get_compose_file() {
    case $1 in
        "dev")
            echo "docker-compose.yml"
            ;;
        "staging")
            echo "docker-compose-staging.yml"
            ;;
        "prod")
            echo "docker-compose-prod.yml"
            ;;
        *)
            echo -e "${RED}❌ Ambiente inválido: $1${NC}"
            show_help
            exit 1
            ;;
    esac
}

# Função para obter porta do ambiente
get_port() {
    case $1 in
        "dev") echo "8013" ;;
        "staging") echo "8020" ;;
        "prod") echo "8021" ;;
    esac
}

# Função para obter URL do ambiente
get_url() {
    case $1 in
        "dev") echo "http://localhost:8013" ;;
        "staging") echo "https://staging.proweb.leoproti.com.br" ;;
        "prod") echo "https://proweb.leoproti.com.br" ;;
    esac
}

# Validar parâmetros
if [ $# -lt 2 ]; then
    echo -e "${RED}❌ Parâmetros insuficientes${NC}"
    show_help
    exit 1
fi

ENVIRONMENT=$1
ACTION=$2
COMPOSE_FILE=$(get_compose_file $ENVIRONMENT)
PORT=$(get_port $ENVIRONMENT)
URL=$(get_url $ENVIRONMENT)

# Verificar se arquivo compose existe
if [ ! -f "$COMPOSE_FILE" ]; then
    echo -e "${RED}❌ Arquivo $COMPOSE_FILE não encontrado!${NC}"
    exit 1
fi

echo -e "${CYAN}🎯 Ambiente: ${YELLOW}$ENVIRONMENT${NC}"
echo -e "${CYAN}📄 Compose: ${YELLOW}$COMPOSE_FILE${NC}"
echo -e "${CYAN}🔌 Porta: ${YELLOW}$PORT${NC}"
echo -e "${CYAN}🌐 URL: ${YELLOW}$URL${NC}"
echo ""

# Executar ação
case $ACTION in
    "up")
        echo -e "${GREEN}🚀 Iniciando ambiente $ENVIRONMENT...${NC}"
        docker-compose -f $COMPOSE_FILE up -d
        echo ""
        echo -e "${GREEN}✅ Ambiente iniciado com sucesso!${NC}"
        echo -e "${CYAN}🌐 Acesse: $URL${NC}"
        echo -e "${CYAN}❤️  Health: $URL/actuator/health${NC}"
        ;;
    
    "down")
        echo -e "${YELLOW}⏹️  Parando ambiente $ENVIRONMENT...${NC}"
        docker-compose -f $COMPOSE_FILE down --remove-orphans
        echo -e "${GREEN}✅ Ambiente parado!${NC}"
        ;;
    
    "logs")
        echo -e "${BLUE}📋 Logs do ambiente $ENVIRONMENT:${NC}"
        docker-compose -f $COMPOSE_FILE logs -f --tail=100
        ;;
    
    "status")
        echo -e "${BLUE}📊 Status do ambiente $ENVIRONMENT:${NC}"
        docker-compose -f $COMPOSE_FILE ps
        echo ""
        echo -e "${CYAN}🔍 Verificando conectividade...${NC}"
        if curl -f -s "$URL/actuator/health" > /dev/null 2>&1; then
            echo -e "${GREEN}✅ Aplicação está respondendo!${NC}"
        else
            echo -e "${RED}❌ Aplicação não está respondendo${NC}"
        fi
        ;;
    
    "restart")
        echo -e "${YELLOW}🔄 Reiniciando ambiente $ENVIRONMENT...${NC}"
        docker-compose -f $COMPOSE_FILE restart
        echo -e "${GREEN}✅ Ambiente reiniciado!${NC}"
        ;;
    
    "build")
        echo -e "${PURPLE}🔨 Rebuild completo do ambiente $ENVIRONMENT...${NC}"
        docker-compose -f $COMPOSE_FILE down --remove-orphans
        docker-compose -f $COMPOSE_FILE build --no-cache
        docker-compose -f $COMPOSE_FILE up -d
        echo -e "${GREEN}✅ Rebuild concluído!${NC}"
        echo -e "${CYAN}🌐 Acesse: $URL${NC}"
        ;;
    
    "config")
        echo -e "${BLUE}⚙️  Configuração do ambiente $ENVIRONMENT:${NC}"
        docker-compose -f $COMPOSE_FILE config
        ;;
    
    "clean")
        echo -e "${RED}🧹 Limpeza completa do ambiente $ENVIRONMENT...${NC}"
        echo -e "${YELLOW}⚠️  Isso irá remover containers, imagens e volumes!${NC}"
        read -p "Deseja continuar? (y/N): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            docker-compose -f $COMPOSE_FILE down --remove-orphans --volumes --rmi all
            echo -e "${GREEN}✅ Limpeza concluída!${NC}"
        else
            echo -e "${BLUE}ℹ️  Operação cancelada${NC}"
        fi
        ;;
    
    *)
        echo -e "${RED}❌ Ação inválida: $ACTION${NC}"
        show_help
        exit 1
        ;;
esac
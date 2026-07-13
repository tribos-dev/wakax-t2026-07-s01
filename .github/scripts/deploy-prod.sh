set -e

: "${ENVIRONMENT:?ENVIRONMENT is required}"
REMOTE_DIR=~/actions-runner-"$ENVIRONMENT"
CONTAINER_NAME=wakax-"$ENVIRONMENT"
BACKUP_FILE=/tmp/deploy_backup_info_"$ENVIRONMENT"
COMPOSE_FILE=docker-compose."$ENVIRONMENT".yml

mkdir -p "$REMOTE_DIR"
cd "$REMOTE_DIR"

if [ ! -f "$COMPOSE_FILE" ]; then
  echo "ERROR: Compose file not found: $COMPOSE_FILE"
  exit 1
fi

# Criar arquivo .env com as variáveis necessárias
# O docker compose lê automaticamente variáveis de um arquivo .env
cat > .env << EOF
IMAGE_NAME=$IMAGE_NAME
ENVIRONMENT=$ENVIRONMENT
WAKAX_PORT=$WAKAX_PORT
HEALTH_URL=$HEALTH_URL
AWS_PARAMETER_STORE_IMPORT=$AWS_PARAMETER_STORE_IMPORT
EOF

echo "Environment variables from .env:"
cat .env

echo "Logging into GHCR"
echo "$GITHUB_TOKEN" | docker login ghcr.io -u "$GITHUB_ACTOR" --password-stdin

# Salvar a imagem atual antes do deploy (backup para rollback)
echo "Saving current image for potential rollback"
CURRENT_IMAGE=$(docker inspect "$CONTAINER_NAME" --format='{{.Config.Image}}' 2>/dev/null || echo "")
BACKUP_IMAGE_ID=""
BACKUP_TAG=""

if [ -n "$CURRENT_IMAGE" ]; then
  echo "Current image: $CURRENT_IMAGE"
  BACKUP_IMAGE_ID=$(docker inspect "$CURRENT_IMAGE" --format='{{.Id}}' 2>/dev/null || echo "")
  if [ -n "$BACKUP_IMAGE_ID" ]; then
    BACKUP_TAG="${ENVIRONMENT}-backup-$(date +%Y%m%d-%H%M%S)"
    docker tag "$BACKUP_IMAGE_ID" "ghcr.io/$IMAGE_NAME:$BACKUP_TAG" 2>/dev/null || echo "Could not tag current image"
    echo "Backup image saved with tag: $BACKUP_TAG (ID: ${BACKUP_IMAGE_ID:0:12})"
  else
    echo "Could not get image ID, rollback may not be possible"
  fi
else
  echo "No existing container found, this appears to be a fresh deployment"
fi

# Salvar informações de backup em arquivo para uso no step de healthcheck
cat > "$BACKUP_FILE" << EOF
CURRENT_IMAGE=$CURRENT_IMAGE
BACKUP_IMAGE_ID=$BACKUP_IMAGE_ID
BACKUP_TAG=$BACKUP_TAG
EOF

echo "Downloading the new image"
docker compose -f "$COMPOSE_FILE" pull

echo "Bringing up the stack"
docker compose -f "$COMPOSE_FILE" up -d --remove-orphans
docker logs "$CONTAINER_NAME"

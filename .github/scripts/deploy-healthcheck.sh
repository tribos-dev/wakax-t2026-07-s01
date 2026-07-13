set -e

: "${ENVIRONMENT:?ENVIRONMENT is required}"
REMOTE_DIR=~/actions-runner-"$ENVIRONMENT"
CONTAINER_NAME=wakax-"$ENVIRONMENT"
BACKUP_FILE=/tmp/deploy_backup_info_"$ENVIRONMENT"
COMPOSE_FILE=docker-compose."$ENVIRONMENT".yml

cleanup() {
  rm -f "$BACKUP_FILE"
}
trap cleanup EXIT

cd "$REMOTE_DIR"

if [ ! -f "$COMPOSE_FILE" ]; then
  echo "ERROR: Compose file not found: $COMPOSE_FILE"
  exit 1
fi

# Carregar informações de backup do step anterior
if [ -f "$BACKUP_FILE" ]; then
  source "$BACKUP_FILE"
  echo "Loaded backup info:"
  echo "  CURRENT_IMAGE: $CURRENT_IMAGE"
  echo "  BACKUP_TAG: $BACKUP_TAG"
  echo "  BACKUP_IMAGE_ID: ${BACKUP_IMAGE_ID:0:12}..."
else
  echo "WARNING: Backup info file not found"
  CURRENT_IMAGE=""
  BACKUP_TAG=""
  BACKUP_IMAGE_ID=""
fi

# Verificar healthcheck do container
echo "Waiting for container healthcheck to pass..."
MAX_WAIT=300  # 5 minutos máximo
WAIT_INTERVAL=10  # Verificar a cada 10 segundos
ELAPSED=0
HEALTHY=false

while [ $ELAPSED -lt $MAX_WAIT ]; do
  # Verificar se o container existe
  if ! docker ps -a --format '{{.Names}}' | grep -q "^$CONTAINER_NAME$"; then
    echo "ERROR: Container $CONTAINER_NAME not found!"
    HEALTHY=false
    break
  fi

  # Verificar status do healthcheck
  HEALTH_STATUS=$(docker inspect "$CONTAINER_NAME" --format='{{.State.Health.Status}}' 2>/dev/null || echo "unknown")
  echo "Health status: $HEALTH_STATUS (elapsed: ${ELAPSED}s)"

  if [ "$HEALTH_STATUS" = "healthy" ]; then
    echo "Container is healthy!"
    HEALTHY=true
    break
  elif [ "$HEALTH_STATUS" = "unhealthy" ]; then
    echo "Container is unhealthy!"
    docker logs "$CONTAINER_NAME"
    HEALTHY=false
    break
  fi

  sleep $WAIT_INTERVAL
  ELAPSED=$((ELAPSED + WAIT_INTERVAL))
done

# Se o healthcheck falhou, fazer rollback
if [ "$HEALTHY" != "true" ]; then
  echo "=========================================="
  echo "HEALTHCHECK FAILED"
  echo "=========================================="

  ROLLBACK_ATTEMPTED=false

  if [ -n "$BACKUP_TAG" ] && [ -n "$CURRENT_IMAGE" ]; then
    echo "Initiating rollback to previous image..."
    echo "Rolling back to: $CURRENT_IMAGE"
    echo "Using backup tag: $BACKUP_TAG"
    ROLLBACK_ATTEMPTED=true

    docker tag "ghcr.io/$IMAGE_NAME:$BACKUP_TAG" "$CURRENT_IMAGE" 2>/dev/null || {
      echo "ERROR: Could not restore backup tag, trying with image ID"
      if [ -n "$BACKUP_IMAGE_ID" ]; then
        docker tag "$BACKUP_IMAGE_ID" "$CURRENT_IMAGE" 2>/dev/null || echo "ERROR: Could not restore using image ID either"
      fi
    }

    # Fazer rollback do container
    echo "Restarting container with previous image..."
    docker compose -f "$COMPOSE_FILE" up -d --remove-orphans --wait --wait-timeout 240

    echo "Waiting for rollback container to stabilize..."
    sleep 60

    echo "Container logs after rollback:"
    docker logs "$CONTAINER_NAME" --tail 50

    # Verificar se o rollback foi bem-sucedido
    ROLLBACK_HEALTH=$(docker inspect "$CONTAINER_NAME" --format='{{.State.Health.Status}}' 2>/dev/null || echo "unknown")
    echo "Rollback container health status: $ROLLBACK_HEALTH"

    echo "=========================================="
    echo "ROLLBACK COMPLETED - PIPELINE FAILING"
    echo "=========================================="
  else
    echo "WARNING: No backup image available for rollback"
    echo "Container may be in an inconsistent state"
    echo "Unable to perform rollback - this appears to be a fresh deployment"

    echo "=========================================="
    echo "DEPLOYMENT FAILED - NO ROLLBACK POSSIBLE"
    echo "=========================================="
  fi

  exit 1
fi

echo "Cleaning up old images"
docker image prune -f

# Remover tag de backup se tudo estiver ok
if [ -n "$BACKUP_TAG" ]; then
  docker rmi "ghcr.io/$IMAGE_NAME:$BACKUP_TAG" 2>/dev/null || echo "Could not remove backup tag"
fi

echo "Healthcheck passed successfully."

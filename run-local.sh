#!/bin/sh

DOCKER_COMPOSE_FILE=docker-compose.yml
PROJECT_NAME=songs

if [[ "$1" == "down" ]]; then
  docker compose -p $PROJECT_NAME -f $DOCKER_COMPOSE_FILE down
elif [[ "$1" == "stop" ]]; then
  docker compose -p $PROJECT_NAME -f $DOCKER_COMPOSE_FILE stop
elif [[ "$1" == "start" ]]; then
  docker compose -p $PROJECT_NAME -f $DOCKER_COMPOSE_FILE start
elif [[ "$1" == "help" ]]; then
  echo "Commands:"
  echo "./run-local.sh       - Will start and restart all the containers."
  echo "./run-local.sh down  - Will stop running containers and remove them, as well as any networks that were created."
  echo "                       This command does not remove any associated volumes."
  echo "./run-local.sh start - Will only restart containers stopped previously."
  echo "./run-local.sh stop  - Will stop running containers but won’t remove them."
  echo "./run-local.sh help  - Displays this help."
else
  docker compose -p $PROJECT_NAME -f $DOCKER_COMPOSE_FILE up -d
fi
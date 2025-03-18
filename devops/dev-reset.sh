#!/bin/bash

echo "🔥 [0/4] REMOVING OLD NAMED CONTAINERS (if exist)..."
docker rm -f zookeeper kafka kafka-ui iot-server 2>/dev/null || true

echo "🔄 [1/4] CLEANING AND BUILDING BACKEND SERVER..."
cd ../ || exit 1
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
  echo "❌ Error during building process."
  exit 1
fi

echo "🧹 [2/4] STOPPING AND ERASING CONTAINERS..."
cd ./devops || exit 1
docker-compose down --volumes --remove-orphans

echo "🚮 [3/4] ERASING BUILD CACHE..."
docker image prune -f

echo "🚀 [4/4] REBOOTING SYSTEM..."
docker-compose up --build

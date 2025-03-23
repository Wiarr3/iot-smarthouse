@echo off

docker rm -f zookeeper kafka kafka-ui iot-server 2>nul

cd ..
mvn clean package -DskipTests

cd devops
docker-compose down --volumes --remove-orphans

docker image prune -f

docker-compose up --build
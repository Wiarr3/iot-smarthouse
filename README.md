# 📡 IoT Smart House – Secure Reactive System

A demonstration project of a secure, reactive IoT network for smart house environments. It showcases data ingestion, processing, monitoring, and bidirectional communication between simulated devices and a frontend web application.

## 🚀 Technologies Used
- **Java 17**, **Spring Boot 3.4.3**
- **Reactive Kafka (reactor-kafka)**
- **Spring WebFlux**
- **Prometheus & Micrometer (metrics monitoring)**
- **Kafka UI (topic visualization)**
- **Docker & Docker Compose**
- **WebSocket (real-time frontend communication)**

## 🏗️ System Architecture Overview

- Simulated IoT devices (e.g., temperature sensor, light bulb) send telemetry data to **Kafka broker**.
- A **Kafka Consumer Service** listens and processes incoming device messages.
- Messages are **verified for authorization**, processed, and optionally broadcast via **WebSocket** to connected clients.
- Commands from frontend apps are sent back to devices via a **Kafka Command Producer**.
- **Prometheus** tracks metrics such as:
    - success/failure rate
    - message processing duration
    - authorization failures

## 📁 Project Structure
### 📁 Project Structure

- devops/
    - dev-reset.sh – helper script to clean containers, build backend, and restart services.
    - docker-compose.yml – infrastructure setup with Kafka, Zookeeper, Kafka UI, and backend server.

- src/main/java/com.smartass.server/
    - config/
        - ReactiveKafkaProducerConfig – Kafka sender configuration.
        - ReactiveKafkaReceiverConfig – Kafka receiver configuration.
        - WebSocketConfig – WebSocket setup.
    - kafka/
        - KafkaCommandProducerService – sends commands to devices.
        - KafkaDeviceDataProducerService – sends simulated telemetry data.
        - KafkaDeviceDataConsumerService – receives and processes device data.
    - model/
        - command/ – DTOs for device commands.
        - device/ – domain models for device telemetry.
        - ErrorReason – error categorization enum.
    - registry/
        - DeviceDeserializerRegistry – mapping deserializers for device types.
    - security/
        - AuthKeyRegistry – manages authentication keys for devices.
    - service/
        - dispatch/
            - DeviceCommandDispatcher – forwards commands to Kafka.
        - processing/
            - DeviceProcessingService – processes validated telemetry.
        - simulator/
            - DeviceDataSimulatorService – runs selected simulators.
            - LightBulbSimulator
            - TemperatureSensorSimulator
            - Simulator – interface for simulators.
            - SimulatorProperties – config for retry/delay.
        - websocket/
            - DeviceCommandWebSocketHandler – WebSocket for command reception.
            - DeviceTelemetryWebSocketHandler – WebSocket for telemetry streaming.
    - ServerApplication – main Spring Boot entry point.

- Dockerfile – backend container definition.
- pom.xml – Maven configuration.
- README.md – documentation.

## ⚙️ Running the System

### 📦 Step-by-step Setup
1. **Build and deploy the backend application**:
```bash
cd devops
chmod +x scripts/build-restart.sh
./scripts/build-restart.sh
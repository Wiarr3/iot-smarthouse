package com.smartass.server.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartass.server.model.entity.*;
import com.smartass.server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnifiedDeviceDataConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final LightBulbDataRepository lightRepo;
    private final FridgeTemperatureSensorRepository fridgeRepo;
    private final MotionSensorDataRepository motionRepo;
    private final SmokeDetectorDataRepository smokeRepo;
    private final TemperatureSensorDataRepository temperatureRepo;
    private final EnergyMeterDataRepository energyRepo;

    @KafkaListener(topics = "device-data", groupId = "sensor-group")
    public void consume(String message) throws Exception {
        JsonNode json = objectMapper.readTree(message);
        String type = json.get("type").asText();

        switch (type) {
            case "light" -> handleLight(json);
            case "fridge" -> handleFridge(json);
            case "motion" -> handleMotion(json);
            case "smoke" -> handleSmoke(json);
            case "temperature" -> handleTemperature(json);
            case "energy" -> handleEnergy(json);
            default -> System.out.println("Unknown type: " + type);
        }
    }

    private void handleLight(JsonNode json) {
        LightBulbDataEntity entity = LightBulbDataEntity.builder()
                .deviceId(json.get("deviceId").asText())
                .timestamp(json.get("timestamp").asLong())
                .state(json.get("state").asBoolean())
                .brightness(json.get("brightness").asInt())
                .authKey(json.get("authKey").asText())
                .build();
        lightRepo.save(entity);
    }

    private void handleFridge(JsonNode json) {
        FridgeTemperatureSensorDataEntity entity = FridgeTemperatureSensorDataEntity.builder()
                .deviceId(json.get("deviceId").asText())
                .timestamp(json.get("timestamp").asLong())
                .temperature(json.get("temperature").asDouble())
                .doorOpen(json.get("doorOpen").asBoolean())
                .authKey(json.get("authKey").asText())
                .build();
        fridgeRepo.save(entity);
    }

    private void handleMotion(JsonNode json) {
        MotionSensorDataEntity entity = MotionSensorDataEntity.builder()
                .deviceId(json.get("deviceId").asText())
                .timestamp(json.get("timestamp").asLong())
                .motionDetected(json.get("motionDetected").asBoolean())
                .authKey(json.get("authKey").asText())
                .build();
        motionRepo.save(entity);
    }

    private void handleSmoke(JsonNode json) {
        SmokeDetectorDataEntity entity = SmokeDetectorDataEntity.builder()
                .deviceId(json.get("deviceId").asText())
                .timestamp(json.get("timestamp").asLong())
                .smokeLevel(json.get("smokeLevel").asDouble())
                .alarmActive(json.get("alarmActive").asBoolean())
                .batteryLevel(json.get("batteryLevel").asDouble())
                .authKey(json.get("authKey").asText())
                .build();
        smokeRepo.save(entity);
    }

    private void handleTemperature(JsonNode json) {
        TemperatureSensorDataEntity entity = TemperatureSensorDataEntity.builder()
                .deviceId(json.get("deviceId").asText())
                .timestamp(json.get("timestamp").asLong())
                .temperature(json.get("temperature").asDouble())
                .humidity(json.get("humidity").asDouble())
                .authKey(json.get("authKey").asText())
                .build();
        temperatureRepo.save(entity);
    }

    private void handleEnergy(JsonNode json) {
        EnergyMeterDataEntity entity = EnergyMeterDataEntity.builder()
                .deviceId(json.get("deviceId").asText())
                .timestamp(json.get("timestamp").asLong())
                .type(json.get("type").asText())
                .currentPower(json.get("currentPower").asDouble())
                .totalEnergy(json.get("totalEnergy").asDouble())
                .authKey(json.get("authKey").asText())
                .build();
        energyRepo.save(entity);
    }
}

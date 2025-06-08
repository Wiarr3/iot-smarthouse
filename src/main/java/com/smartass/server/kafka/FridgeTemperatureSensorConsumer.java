package com.smartass.server.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartass.server.model.entity.FridgeTemperatureSensorDataEntity;
import com.smartass.server.repository.FridgeTemperatureSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;

@Service
@RequiredArgsConstructor
public class FridgeTemperatureSensorConsumer {
    private final FridgeTemperatureSensorRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "fridge-temperature", groupId = "sensor-group")
    public void consume(String message) throws Exception {
        JsonNode json = mapper.readTree(message);
        FridgeTemperatureSensorDataEntity entity = FridgeTemperatureSensorDataEntity.builder()
                .deviceId(json.get("deviceId").asText())
                .timestamp(json.get("timestamp").asLong())
                .temperature(json.get("temperature").asDouble())
                .doorOpen(json.get("doorOpen").asBoolean())
                .authKey(json.get("authKey").asText())
                .build();
        repository.save(entity);
    }
}

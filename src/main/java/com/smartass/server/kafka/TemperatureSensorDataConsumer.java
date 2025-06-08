package com.smartass.server.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartass.server.model.entity.TemperatureSensorDataEntity;
import com.smartass.server.repository.TemperatureSensorDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;

@Service
@RequiredArgsConstructor
public class TemperatureSensorDataConsumer {
    private final TemperatureSensorDataRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "temperature-sensor", groupId = "sensor-group")
    public void consume(String message) throws Exception {
        JsonNode json = mapper.readTree(message);
        TemperatureSensorDataEntity entity = TemperatureSensorDataEntity.builder()
                .deviceId(json.get("deviceId").asText())
                .timestamp(json.get("timestamp").asLong())
                .temperature(json.get("temperature").asDouble())
                .humidity(json.get("humidity").asDouble())
                .authKey(json.get("authKey").asText())
                .build();
        repository.save(entity);
    }
}

package com.smartass.server.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartass.server.model.entity.SmokeDetectorDataEntity;
import com.smartass.server.repository.SmokeDetectorDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;

@Service
@RequiredArgsConstructor
public class SmokeDetectorDataConsumer {
    private final SmokeDetectorDataRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "smoke-detector", groupId = "sensor-group")
    public void consume(String message) throws Exception {
        JsonNode json = mapper.readTree(message);
        SmokeDetectorDataEntity entity = SmokeDetectorDataEntity.builder()
                .deviceId(json.get("deviceId").asText())
                .timestamp(json.get("timestamp").asLong())
                .smokeLevel(json.get("smokeLevel").asDouble())
                .alarmActive(json.get("alarmActive").asBoolean())
                .batteryLevel(json.get("batteryLevel").asDouble())
                .authKey(json.get("authKey").asText())
                .build();
        repository.save(entity);
    }
}

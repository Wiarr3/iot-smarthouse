package com.smartass.server.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartass.server.model.entity.MotionSensorDataEntity;
import com.smartass.server.repository.MotionSensorDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;

@Service
@RequiredArgsConstructor
public class MotionSensorDataConsumer {
    private final MotionSensorDataRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "motion-sensor", groupId = "sensor-group")
    public void consume(String message) throws Exception {
        JsonNode json = mapper.readTree(message);
        MotionSensorDataEntity entity = MotionSensorDataEntity.builder()
                .deviceId(json.get("deviceId").asText())
                .timestamp(json.get("timestamp").asLong())
                .motionDetected(json.get("motionDetected").asBoolean())
                .authKey(json.get("authKey").asText())
                .build();
        repository.save(entity);
    }
}

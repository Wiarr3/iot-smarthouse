package com.smartass.server.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartass.server.model.entity.LightBulbDataEntity;
import com.smartass.server.repository.LightBulbDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;

@Service
@RequiredArgsConstructor
public class LightBulbDataConsumer {
    private final LightBulbDataRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "light-bulb", groupId = "sensor-group")
    public void consume(String message) throws Exception {
        JsonNode json = mapper.readTree(message);
        LightBulbDataEntity entity = LightBulbDataEntity.builder()
                .deviceId(json.get("deviceId").asText())
                .timestamp(json.get("timestamp").asLong())
                .state(json.get("state").asBoolean())
                .brightness(json.get("brightness").asInt())
                .authKey(json.get("authKey").asText())
                .build();
        repository.save(entity);
    }
}

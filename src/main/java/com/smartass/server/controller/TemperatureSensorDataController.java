package com.smartass.server.controller;

import com.smartass.server.model.device.TemperatureSensorData;
import com.smartass.server.model.entity.TemperatureSensorDataEntity;
import com.smartass.server.repository.TemperatureSensorDataRepository;
import com.smartass.server.service.alert.AlertReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/temperature-sensor")
@RequiredArgsConstructor
public class TemperatureSensorDataController {

    private final TemperatureSensorDataRepository repository;
    private final AlertReactionService alertReactionService;

    @GetMapping
    public List<TemperatureSensorDataEntity> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public void save(@RequestBody TemperatureSensorData data) {
        repository.save(
                TemperatureSensorDataEntity.builder()
                        .deviceId(data.getDeviceId())
                        .timestamp(data.getTimestamp())
                        .temperature(data.getTemperature())
                        .humidity(data.getHumidity())
                        .authKey(data.getAuthKey())
                        .build()
        );

        alertReactionService.evaluateAndReact(data);
    }
}

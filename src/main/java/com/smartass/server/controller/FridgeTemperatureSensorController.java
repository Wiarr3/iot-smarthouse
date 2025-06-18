package com.smartass.server.controller;

import com.smartass.server.model.device.FridgeTemperatureSensorData;
import com.smartass.server.model.entity.FridgeTemperatureSensorDataEntity;
import com.smartass.server.repository.FridgeTemperatureSensorRepository;
import com.smartass.server.service.alert.AlertReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fridge-sensor")
@RequiredArgsConstructor
public class FridgeTemperatureSensorController {

    private final FridgeTemperatureSensorRepository repository;
    private final AlertReactionService alertReactionService;

    @GetMapping
    public List<FridgeTemperatureSensorDataEntity> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public void save(@RequestBody FridgeTemperatureSensorData data) {
        repository.save(
                FridgeTemperatureSensorDataEntity.builder()
                        .deviceId(data.getDeviceId())
                        .timestamp(data.getTimestamp())
                        .temperature(data.getTemperature())
                        .doorOpen(data.getDoorOpen())
                        .authKey(data.getAuthKey())
                        .build()
        );

        alertReactionService.evaluateAndReact(data);
    }
}

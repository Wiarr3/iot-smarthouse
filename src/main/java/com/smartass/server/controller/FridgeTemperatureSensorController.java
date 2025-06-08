package com.smartass.server.controller;

import com.smartass.server.model.entity.FridgeTemperatureSensorDataEntity;
import com.smartass.server.repository.FridgeTemperatureSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fridge-temperature")
@RequiredArgsConstructor
public class FridgeTemperatureSensorController {

    private final FridgeTemperatureSensorRepository repository;

    @GetMapping
    public List<FridgeTemperatureSensorDataEntity> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public FridgeTemperatureSensorDataEntity post(@RequestBody FridgeTemperatureSensorDataEntity entity) {
        return repository.save(entity);
    }
}

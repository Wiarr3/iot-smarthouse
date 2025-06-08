package com.smartass.server.controller;

import com.smartass.server.model.entity.TemperatureSensorDataEntity;
import com.smartass.server.repository.TemperatureSensorDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/temperature-sensor")
@RequiredArgsConstructor
public class TemperatureSensorDataController {

    private final TemperatureSensorDataRepository repository;

    @GetMapping
    public List<TemperatureSensorDataEntity> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public TemperatureSensorDataEntity post(@RequestBody TemperatureSensorDataEntity entity) {
        return repository.save(entity);
    }
}

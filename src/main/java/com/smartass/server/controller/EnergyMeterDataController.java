package com.smartass.server.controller;

import com.smartass.server.model.entity.EnergyMeterDataEntity;
import com.smartass.server.repository.EnergyMeterDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/energy-meter")
@RequiredArgsConstructor
public class EnergyMeterDataController {

    private final EnergyMeterDataRepository repository;

    @GetMapping
    public List<EnergyMeterDataEntity> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public EnergyMeterDataEntity post(@RequestBody EnergyMeterDataEntity entity) {
        return repository.save(entity);
    }
}

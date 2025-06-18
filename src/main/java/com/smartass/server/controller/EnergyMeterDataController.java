package com.smartass.server.controller;

import com.smartass.server.model.device.EnergyMeterData;
import com.smartass.server.model.entity.EnergyMeterDataEntity;
import com.smartass.server.repository.EnergyMeterDataRepository;
import com.smartass.server.service.alert.AlertReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/energy-meter")
@RequiredArgsConstructor
public class EnergyMeterDataController {

    private final EnergyMeterDataRepository repository;
    private final AlertReactionService alertReactionService;

    @GetMapping
    public List<EnergyMeterDataEntity> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public void save(@RequestBody EnergyMeterData data) {
        repository.save(
                EnergyMeterDataEntity.builder()
                        .deviceId(data.getDeviceId())
                        .timestamp(data.getTimestamp())
                        .currentPower(data.getCurrentPower())
                        .totalEnergy(data.getTotalEnergy())
                        .type(data.getType())
                        .authKey(data.getAuthKey())
                        .build()
        );

        alertReactionService.evaluateAndReact(data);
    }
}

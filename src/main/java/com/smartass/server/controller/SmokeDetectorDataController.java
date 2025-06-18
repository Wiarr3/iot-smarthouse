package com.smartass.server.controller;

import com.smartass.server.model.device.SmokeDetectorData;
import com.smartass.server.model.entity.SmokeDetectorDataEntity;
import com.smartass.server.repository.SmokeDetectorDataRepository;
import com.smartass.server.service.alert.AlertReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/smoke-detector")
@RequiredArgsConstructor
public class SmokeDetectorDataController {

    private final SmokeDetectorDataRepository repository;
    private final AlertReactionService alertReactionService;

    @GetMapping
    public List<SmokeDetectorDataEntity> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public void save(@RequestBody SmokeDetectorData data) {
        repository.save(
                SmokeDetectorDataEntity.builder()
                        .deviceId(data.getDeviceId())
                        .timestamp(data.getTimestamp())
                        .smokeLevel(data.getSmokeLevel())
                        .alarmActive(data.getAlarmActive())
                        .batteryLevel(data.getBatteryLevel())
                        .authKey(data.getAuthKey())
                        .build()
        );

        alertReactionService.evaluateAndReact(data);
    }
}

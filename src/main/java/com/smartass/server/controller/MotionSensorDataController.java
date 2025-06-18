package com.smartass.server.controller;

import com.smartass.server.model.device.MotionSensorData;
import com.smartass.server.model.entity.MotionSensorDataEntity;
import com.smartass.server.repository.MotionSensorDataRepository;
import com.smartass.server.service.alert.AlertReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/motion-sensor")
@RequiredArgsConstructor
public class MotionSensorDataController {

    private final MotionSensorDataRepository repository;
    private final AlertReactionService alertReactionService;

    @GetMapping
    public List<MotionSensorDataEntity> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public void save(@RequestBody MotionSensorData data) {
        repository.save(
                MotionSensorDataEntity.builder()
                        .deviceId(data.getDeviceId())
                        .timestamp(data.getTimestamp())
                        .motionDetected(data.getMotionDetected())
                        .authKey(data.getAuthKey())
                        .build()
        );

        alertReactionService.evaluateAndReact(data);
    }
}

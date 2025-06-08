package com.smartass.server.controller;

import com.smartass.server.model.entity.MotionSensorDataEntity;
import com.smartass.server.repository.MotionSensorDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/motion-sensor")
@RequiredArgsConstructor
public class MotionSensorDataController {

    private final MotionSensorDataRepository repository;

    @GetMapping
    public List<MotionSensorDataEntity> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public MotionSensorDataEntity post(@RequestBody MotionSensorDataEntity entity) {
        return repository.save(entity);
    }
}

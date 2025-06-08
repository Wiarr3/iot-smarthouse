package com.smartass.server.controller;

import com.smartass.server.model.entity.SmokeDetectorDataEntity;
import com.smartass.server.repository.SmokeDetectorDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/smoke-detector")
@RequiredArgsConstructor
public class SmokeDetectorDataController {

    private final SmokeDetectorDataRepository repository;

    @GetMapping
    public List<SmokeDetectorDataEntity> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public SmokeDetectorDataEntity post(@RequestBody SmokeDetectorDataEntity entity) {
        return repository.save(entity);
    }
}

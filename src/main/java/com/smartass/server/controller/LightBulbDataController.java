package com.smartass.server.controller;

import com.smartass.server.model.entity.LightBulbDataEntity;
import com.smartass.server.repository.LightBulbDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/light-bulb")
@RequiredArgsConstructor
public class LightBulbDataController {

    private final LightBulbDataRepository repository;

    @GetMapping
    public List<LightBulbDataEntity> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public LightBulbDataEntity post(@RequestBody LightBulbDataEntity entity) {
        return repository.save(entity);
    }
}

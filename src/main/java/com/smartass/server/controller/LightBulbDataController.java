package com.smartass.server.controller;

import com.smartass.server.model.device.LightBulbData;
import com.smartass.server.model.entity.LightBulbDataEntity;
import com.smartass.server.repository.LightBulbDataRepository;
import com.smartass.server.service.alert.AlertReactionService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/light-bulb")
@RequiredArgsConstructor
public class LightBulbDataController {

    private final LightBulbDataRepository repository;
    private final AlertReactionService alertReactionService;

    @GetMapping
    public List<LightBulbDataEntity> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public void save(@RequestBody LightBulbData data) {
        repository.save(
                LightBulbDataEntity.builder()
                        .deviceId(data.getDeviceId())
                        .timestamp(data.getTimestamp())
                        .state(data.getState())
                        .brightness(data.getBrightness())
                        .authKey(data.getAuthKey())
                        .build()
        );

        alertReactionService.evaluateAndReact(data);
    }
}

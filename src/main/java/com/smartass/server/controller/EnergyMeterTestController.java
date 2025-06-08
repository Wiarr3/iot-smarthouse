package com.smartass.server.controller;

import com.smartass.server.model.entity.EnergyMeterDataEntity;
import com.smartass.server.repository.EnergyMeterDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test/energy")
public class EnergyMeterTestController {

    private final EnergyMeterDataRepository repository;

    public EnergyMeterTestController(EnergyMeterDataRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/add")
    public EnergyMeterDataEntity addSample(@RequestBody EnergyMeterDataEntity data) {
        return repository.save(data);
    }

    @GetMapping("/all")
    public List<EnergyMeterDataEntity> getAll() {
        return repository.findAll();
    }
}

package com.smartass.server.controller;

import com.smartass.server.model.entity.AlertEventEntity;
import com.smartass.server.repository.AlertEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertEventRepository repository;

    @GetMapping
    public List<AlertEventEntity> getAll() {
        return repository.findAll();
    }
}

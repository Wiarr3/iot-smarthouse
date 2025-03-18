package com.smartass.server.controller;

import com.smartass.server.model.alert.AlertCondition;
import com.smartass.server.registry.ConditionRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alert/condition")
public class ConditionController {
    ConditionRegistry conditionRegistry;

    public ConditionController(ConditionRegistry conditionRegistry) {
        this.conditionRegistry = conditionRegistry;
    }

    @GetMapping
    public ResponseEntity<List<AlertCondition>> getAllConditions() {
        return ResponseEntity.ok(conditionRegistry.getAllConditions().values().stream().toList());
    }

    @GetMapping("/{deviceName}/{paramName}")
    public ResponseEntity<AlertCondition> getCondition(@PathVariable int deviceName, @PathVariable String paramName) {
        return ResponseEntity.ok(conditionRegistry.getCondition(deviceName + "-" + paramName));
    }

    @PostMapping
    public ResponseEntity<Void> createCondition(@RequestBody AlertCondition condition) {
        conditionRegistry.setCondition(condition);
        return ResponseEntity.ok(null);
    }
}

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
    public ResponseEntity<AlertCondition> getCondition(@PathVariable String deviceName, @PathVariable String paramName) {
        AlertCondition condition = conditionRegistry.getCondition(deviceName + "-" + paramName);
        if (condition == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(condition);
    }

    @PostMapping
    public ResponseEntity<AlertCondition> createCondition(@RequestBody AlertCondition condition) {
        conditionRegistry.setCondition(condition);
        return ResponseEntity.ok(condition);
    }
}

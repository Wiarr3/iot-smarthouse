package com.smartass.server.controller;

import com.smartass.server.model.alert.AlertCondition;
import com.smartass.server.registry.ConditionRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/conditions")
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
    public ResponseEntity<?> createCondition(@RequestBody AlertCondition condition) {
        try {
            conditionRegistry.setCondition(condition);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }

        return ResponseEntity.ok(condition);
    }
}

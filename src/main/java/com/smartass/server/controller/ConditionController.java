package com.smartass.server.controller;

import com.smartass.server.model.alert.AlertCondition;
import com.smartass.server.registry.ConditionRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alert/condition")
public class ConditionController {
    private final ConditionRegistry conditionRegistry;

    public ConditionController(ConditionRegistry conditionRegistry) {
        this.conditionRegistry = conditionRegistry;
    }

    @GetMapping
    public ResponseEntity<List<AlertCondition>> getAllConditions() {
        return ResponseEntity.ok(conditionRegistry.getAllConditions().values().stream().toList());
    }
    @GetMapping("/{deviceType}")
    public ResponseEntity<List<AlertCondition>> getConditionByType(@PathVariable String deviceType) {
        return ResponseEntity.ok(conditionRegistry.getConditionsByDeviceType(deviceType));
    }

    @GetMapping("/{deviceType}/{paramName}")
    public ResponseEntity<AlertCondition> getCondition(@PathVariable String deviceType, @PathVariable String paramName) {
        String key = deviceType + "-" + paramName;
        AlertCondition condition = conditionRegistry.getCondition(key);
        if (condition == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(condition);
    }

    @PostMapping
    public ResponseEntity<?> createCondition(@RequestBody AlertCondition condition) {
        try {
            conditionRegistry.setCondition(condition);
            return ResponseEntity.ok(condition);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{deviceType}/{paramName}")
    public ResponseEntity<?> updateCondition(@PathVariable String deviceType,
                                             @PathVariable String paramName,
                                             @RequestBody AlertCondition updatedCondition) {
        String key = deviceType + "-" + paramName;
        if (conditionRegistry.getCondition(key) == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            conditionRegistry.setCondition(updatedCondition);
            return ResponseEntity.ok(updatedCondition);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{deviceType}/{paramName}")
    public ResponseEntity<?> deleteCondition(@PathVariable String deviceType, @PathVariable String paramName) {
        String key = deviceType + "-" + paramName;
        AlertCondition existing = conditionRegistry.getCondition(key);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        conditionRegistry.deleteCondition(key);
        return ResponseEntity.noContent().build();
    }
}

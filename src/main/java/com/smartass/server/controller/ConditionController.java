package com.smartass.server.controller;

import com.smartass.server.model.alert.AlertCondition;
import com.smartass.server.registry.ConditionRegistry;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<AlertCondition> getAllConditions() {
        return Flux.fromIterable(conditionRegistry.getAllConditions().values());
    }

    @GetMapping("/{deviceType}")
    public Flux<AlertCondition> getConditionByType(@PathVariable String deviceType) {
        return Flux.fromIterable(conditionRegistry.getConditionsByDeviceType(deviceType));
    }

    @GetMapping("/{deviceType}/{paramName}")
    public Mono<AlertCondition> getCondition(@PathVariable String deviceType, @PathVariable String paramName) {
        String key = deviceType + "-" + paramName;
        AlertCondition condition = conditionRegistry.getCondition(key);
        return condition != null ? Mono.just(condition) : Mono.empty();
    }

    @PostMapping
    public Mono<?> createCondition(@RequestBody AlertCondition condition) {
        return Mono.fromCallable(() -> {
            try {
                conditionRegistry.setCondition(condition);
                return condition;
            } catch (Exception e) {
                return Map.of("error", e.getMessage());
            }
        });
    }

    @PutMapping("/{deviceType}/{paramName}")
    public Mono<?> updateCondition(@PathVariable String deviceType,
                                   @PathVariable String paramName,
                                   @RequestBody AlertCondition updatedCondition) {
        String key = deviceType + "-" + paramName;
        if (conditionRegistry.getCondition(key) == null) {
            return Mono.empty(); // 404
        }
        return Mono.fromCallable(() -> {
            try {
                conditionRegistry.setCondition(updatedCondition);
                return updatedCondition;
            } catch (Exception e) {
                return Map.of("error", e.getMessage());
            }
        });
    }

    @DeleteMapping("/{deviceType}/{paramName}")
    public Mono<Void> deleteCondition(@PathVariable String deviceType, @PathVariable String paramName) {
        String key = deviceType + "-" + paramName;
        AlertCondition existing = conditionRegistry.getCondition(key);
        if (existing == null) {
            return Mono.empty(); // 404
        }
        conditionRegistry.deleteCondition(key);
        return Mono.empty(); // NoContent
    }
}

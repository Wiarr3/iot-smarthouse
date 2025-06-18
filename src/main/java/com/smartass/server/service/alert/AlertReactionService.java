package com.smartass.server.service.alert;

import com.smartass.server.model.alert.AlertCondition;
import com.smartass.server.model.entity.AlertEventEntity;
import com.smartass.server.model.device.DeviceData;
import com.smartass.server.repository.AlertConditionRepository;
import com.smartass.server.repository.AlertEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertReactionService {

    private final AlertConditionRepository conditionRepo;
    private final AlertEventRepository alertEventRepo;

    public List<AlertEventEntity> evaluateAndReact(DeviceData data) {
        List<AlertCondition> allConditions = conditionRepo.findAll();
        List<AlertEventEntity> triggered = new ArrayList<>();

        for (AlertCondition condition : allConditions) {
            if (!condition.getDeviceType().equalsIgnoreCase(data.getType())) continue;

            if (evaluate(data, condition)) {
                AlertEventEntity alert = AlertEventEntity.builder()
                        .deviceId(data.getDeviceId())
                        .parameter(condition.getParameter())
                        .value(data.getValueFor(condition.getParameter()))
                        .operator(condition.getOperator().name())
                        .severity(condition.getSeverity())
                        .description(condition.getDescription())
                        .timestamp(Instant.now())
                        .build();

                alertEventRepo.save(alert);
                triggered.add(alert);
            }
        }

        return triggered;
    }

    private boolean evaluate(DeviceData data, AlertCondition condition) {
        try {
            double value = Double.parseDouble(data.getValueFor(condition.getParameter()));
            double threshold = Double.parseDouble(condition.getValue());

            return switch (condition.getOperator()) {
                case GREATER_THAN -> value > threshold;
                case LESS_THAN -> value < threshold;
                case EQUALS -> value == threshold;
                default -> false; // Covers all unexpected cases
            };

        } catch (Exception e) {
            return false;
        }
    }
}

package com.smartass.server.registry;

import com.smartass.server.model.alert.AlertCondition;
import com.smartass.server.model.alert.AlertSeverity;
import com.smartass.server.model.alert.ComparisonOperator;
import com.smartass.server.model.device.DeviceData;
import com.smartass.server.service.alert.AlertConditionValidator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConditionRegistry {
    /// In current version of the system there is only one permitted condition per parameter
    private final Map<String, AlertCondition> conditions = new ConcurrentHashMap<>();
    private final AlertConditionValidator alertConditionValidator;

    public ConditionRegistry(AlertConditionValidator alertConditionValidator) {
        this.alertConditionValidator = alertConditionValidator;
        conditions.put("temperature-high", new AlertCondition("temperature", "temperature",
                AlertSeverity.WARNING, ComparisonOperator.GREATER_THAN, "15",
                "Temperature is too high!"));
        conditions.put("temperature-low", new AlertCondition("temperature", "temperature",
                AlertSeverity.WARNING, ComparisonOperator.LESS_THAN, "17",
                "Temperature is too low!"));
        conditions.put("light", new AlertCondition("light", "state", AlertSeverity.CRITICAL,
                ComparisonOperator.EQUALS, "ON",
                "Light switch should be on!"));
        conditions.put("energy-high", new AlertCondition("energy", "currentPower",
                AlertSeverity.WARNING, ComparisonOperator.GREATER_THAN, "300",
                "Temperature is too low!"));
        conditions.put("fridge-temperature-high", new AlertCondition("fridge", "temperature",
                AlertSeverity.WARNING, ComparisonOperator.GREATER_THAN, "12",
                "Temperature in fridge is too high!"));
        conditions.put("smoke", new AlertCondition("smoke", "alarmActive", AlertSeverity.CRITICAL,
                ComparisonOperator.EQUALS, "ON",
                "The sensor has detected smoke!"));
    }

    public AlertCondition getCondition(String parameter) {
        return conditions.get(parameter);
    }

    public void setCondition(AlertCondition condition) {
        if (alertConditionValidator.validate(condition)) {
            String conditionID = condition.getDeviceType() + "-" + condition.getParameter();
            conditions.put(conditionID, condition);
        } else {
            throw new IllegalArgumentException("Invalid condition: " + condition);
        }
    }

    public Map<String, AlertCondition> getAllConditions() {
        return Map.copyOf(conditions);
    }

}

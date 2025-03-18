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
        conditions.put("temperature", new AlertCondition("temperature", "temperature",
                AlertSeverity.WARNING, ComparisonOperator.GREATER_THAN, "70",
                "Temperature is too high!"));
        conditions.put("light", new AlertCondition("light", "state", AlertSeverity.CRITICAL,
                ComparisonOperator.EQUALS, "ON",
                "Light switch should be on!"));
    }

    public AlertCondition getCondition(String parameter) {
        return conditions.get(parameter);
    }

    public void setCondition(AlertCondition condition) {
        if (alertConditionValidator.validate(condition)) {
            String conditionID = condition.getDeviceType() + "-" + condition.getParameter();
            conditions.put(conditionID, condition);
        }
    }

    public Map<String, AlertCondition> getAllConditions() {
        return Map.copyOf(conditions);
    }

}

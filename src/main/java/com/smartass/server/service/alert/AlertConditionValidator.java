package com.smartass.server.service.alert;

import com.smartass.server.model.alert.AlertCondition;
import com.smartass.server.model.alert.ComparisonOperator;
import com.smartass.server.model.device.DeviceData;
import com.smartass.server.registry.DeviceDeserializerRegistry;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class AlertConditionValidator {
    private final DeviceDeserializerRegistry deviceDeserializerRegistry;

    public AlertConditionValidator(DeviceDeserializerRegistry deviceDeserializerRegistry) {
        this.deviceDeserializerRegistry = deviceDeserializerRegistry;
    }

    public boolean validate(AlertCondition alertCondition) {
        return validateOperator(alertCondition) && validateConditionForDeviceType(alertCondition.getDeviceType(), alertCondition);
    }

    private boolean validateOperator(AlertCondition condition) {
        ComparisonOperator op = condition.getOperator();
        if (requiresNumericValue(op)) {
            try {
                Double.parseDouble(condition.getValue());
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    public boolean validateConditionForDeviceType(String deviceType, AlertCondition condition) {
        Class<? extends DeviceData> clazz = deviceDeserializerRegistry.resolve(deviceType);
        if (clazz == null) {
            return false;
        }

        try {
            var field = clazz.getDeclaredField(condition.getParameter());

            if (requiresNumericValue(condition.getOperator())) {
                if (!Number.class.isAssignableFrom(field.getType()) && !field.getType().isPrimitive()) {
                    return false;
                }

                try {
                    Double.parseDouble(condition.getValue());
                } catch (NumberFormatException e) {
                    return false;
                }
            }

            if (isBooleanField(field) && isBooleanOperator(condition.getOperator())) {
                return condition.getValue().equalsIgnoreCase("true") ||
                        condition.getValue().equalsIgnoreCase("false");
            }

            return true;

        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    private boolean requiresNumericValue(ComparisonOperator op) {
        return switch (op) {
            case GREATER_THAN, GREATER_OR_EQUAL, LESS_THAN, LESS_OR_EQUAL -> true;
            default -> false;
        };
    }

    private boolean isBooleanOperator(ComparisonOperator op) {
        return op == ComparisonOperator.EQUALS || op == ComparisonOperator.NOT_EQUALS;
    }

    private boolean isBooleanField(Field field) {
        return field.getType().equals(Boolean.class) || field.getType().equals(boolean.class);
    }
}

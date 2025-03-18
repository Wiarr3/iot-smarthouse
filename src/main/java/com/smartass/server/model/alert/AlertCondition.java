package com.smartass.server.model.alert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlertCondition {
    private String deviceType;
    private String parameter;
    private AlertSeverity severity;
    private ComparisonOperator operator;
    private String value;
    private String description;
}
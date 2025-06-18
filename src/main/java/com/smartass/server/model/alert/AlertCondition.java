package com.smartass.server.model.alert;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "alert_conditions")
public class AlertCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceType;
    private String parameter;

    @Enumerated(EnumType.STRING)
    private AlertSeverity severity;

    @Enumerated(EnumType.STRING)
    private ComparisonOperator operator;

    private String value;
    private String description;

    // ✅ Add this 6-argument constructor to match your usage in ConditionRegistry
    public AlertCondition(String deviceType, String parameter, AlertSeverity severity, ComparisonOperator operator, String value, String description) {
        this.deviceType = deviceType;
        this.parameter = parameter;
        this.severity = severity;
        this.operator = operator;
        this.value = value;
        this.description = description;
    }
}

package com.smartass.server.model.entity;

import com.smartass.server.model.alert.AlertSeverity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alerts")
public class AlertEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceId;
    private String parameter;
    private String value;
    private String operator;
    private String description;

    @Enumerated(EnumType.STRING)
    private AlertSeverity severity;

    private Instant timestamp;
}

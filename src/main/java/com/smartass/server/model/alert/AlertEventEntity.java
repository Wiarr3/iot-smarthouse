package com.smartass.server.model.alert;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceId;
    private String parameter;
    private String value;
    private String operator;

    @Enumerated(EnumType.STRING)
    private AlertSeverity severity;

    private String description;

    private Instant timestamp;
}

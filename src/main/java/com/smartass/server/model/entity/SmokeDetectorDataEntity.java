package com.smartass.server.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmokeDetectorDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String deviceId;
    private Long timestamp;
    private Double smokeLevel;
    private Boolean alarmActive;
    private Double batteryLevel;
    private String authKey;


}

package com.smartass.server.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FridgeTemperatureSensorDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String deviceId;
    private Long timestamp;
    private Double temperature;
    private Boolean doorOpen;
    private String authKey;
}

package com.smartass.server.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnergyMeterDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceId;
    private String type;
    private long timestamp;
    private double currentPower;
    private double totalEnergy;
    private String authKey;
}

package com.smartass.server.model.device;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnergyMeterData implements DeviceData {
    private String deviceId;
    private String authKey;
    private String type;
    private Long timestamp;
    private Double currentPower;
    private Double totalEnergy;

    @Override
    public String getValueFor(String parameter) {
        return switch (parameter) {
            case "currentPower" -> String.valueOf(this.currentPower);
            case "totalEnergy" -> String.valueOf(this.totalEnergy);
            default -> throw new IllegalArgumentException("Unknown parameter: " + parameter);
        };
    }
}

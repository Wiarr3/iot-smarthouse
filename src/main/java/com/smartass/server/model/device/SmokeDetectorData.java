package com.smartass.server.model.device;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmokeDetectorData implements DeviceData {
    private String deviceId;
    private String authKey;
    private String type;
    private Long timestamp;
    private Double smokeLevel;
    private Boolean alarmActive;
    private Double batteryLevel;

    @Override
    public String getValueFor(String parameter) {
        return switch (parameter) {
            case "smokeLevel" -> String.valueOf(this.smokeLevel);
            case "alarmActive" -> String.valueOf(this.alarmActive);
            case "batteryLevel" -> String.valueOf(this.batteryLevel);
            default -> throw new IllegalArgumentException("Unknown parameter: " + parameter);
        };
    }
}

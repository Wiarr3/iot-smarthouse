package com.smartass.server.model.device;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FridgeTemperatureSensorData implements DeviceData {
    private String deviceId;
    private String authKey;
    private String type;
    private Long timestamp;
    private Double temperature;
    private Boolean doorOpen;

    @Override
    public String getValueFor(String parameter) {
        return switch (parameter) {
            case "temperature" -> String.valueOf(this.temperature);
            case "doorOpen" -> String.valueOf(this.doorOpen);
            default -> throw new IllegalArgumentException("Unknown parameter: " + parameter);
        };
    }
}

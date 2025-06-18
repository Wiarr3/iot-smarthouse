package com.smartass.server.model.device;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemperatureSensorData implements DeviceData {
    private String deviceId;
    private String authKey;
    private String type;
    private Long timestamp;
    private Double temperature;
    private Double humidity;

    @Override
    public String getValueFor(String parameter) {
        return switch (parameter) {
            case "temperature" -> String.valueOf(this.temperature);
            case "humidity" -> String.valueOf(this.humidity);
            default -> throw new IllegalArgumentException("Unknown parameter: " + parameter);
        };
    }
}

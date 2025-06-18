package com.smartass.server.model.device;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MotionSensorData implements DeviceData {
    private String deviceId;
    private String authKey;
    private String type;
    private Long timestamp;
    private Boolean motionDetected;

    @Override
    public String getValueFor(String parameter) {
        return switch (parameter) {
            case "motionDetected" -> String.valueOf(this.motionDetected);
            default -> throw new IllegalArgumentException("Unknown parameter: " + parameter);
        };
    }
}

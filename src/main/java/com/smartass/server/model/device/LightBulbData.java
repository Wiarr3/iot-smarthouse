package com.smartass.server.model.device;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LightBulbData implements DeviceData {
    private String deviceId;
    private String authKey;
    private String type;
    private Long timestamp;
    private Boolean state;
    private Integer brightness;

    @Override
    public String getValueFor(String parameter) {
        return switch (parameter) {
            case "state" -> String.valueOf(this.state);
            case "brightness" -> String.valueOf(this.brightness);
            default -> throw new IllegalArgumentException("Unknown parameter: " + parameter);
        };
    }
}

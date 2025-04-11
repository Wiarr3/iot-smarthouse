package com.smartass.server.model.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
package com.smartass.server.model.device;

public interface DeviceData {
    String getDeviceId();
    String getType();
    Long getTimestamp();
    String getAuthKey();
}

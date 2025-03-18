package com.smartass.server.service.processing;

import com.smartass.server.model.device.DeviceData;
import com.smartass.server.model.device.LightBulbData;
import com.smartass.server.model.device.TemperatureSensorData;
import com.smartass.server.service.alert.AlertReactionService;
import org.springframework.stereotype.Service;

@Service
public class DeviceProcessingService {

    private final AlertReactionService alertReactionService;

    public DeviceProcessingService(AlertReactionService alertReactionService) {
        this.alertReactionService = alertReactionService;
    }

    public void handleGeneric(DeviceData data) {

        switch (data.getType()) {
            case "temperature" -> handleTemperature((TemperatureSensorData) data);
            case "light" -> handleLight((LightBulbData) data);
            default -> System.out.println("Unknown device type");
        }
    }
    private void handleTemperature(TemperatureSensorData data) {
        System.out.println("[TEMPERATURE] " + data.getTemperature() + "C, humidity: " + data.getHumidity());

    }
    private void handleLight(LightBulbData data) {
        System.out.println("[LIGHT] Status: " + (data.getState() ? "ON" : "OFF") + ", Brightness: " + data.getBrightness());

    }
}

package com.smartass.server.service.alert;

import com.smartass.server.model.device.DeviceData;
import com.smartass.server.model.device.TemperatureSensorData;
import com.smartass.server.model.device.LightBulbData;
import com.smartass.server.model.alert.AlertDTO;
import com.smartass.server.model.alert.AlertSeverity;
import com.smartass.server.model.alert.AlertType;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AlertRuleEngine {

    public Optional<AlertDTO> evaluate(DeviceData data) {
        if (data instanceof TemperatureSensorData temp) {
            if (temp.getTemperature() != null && temp.getTemperature() > 70) {
                return Optional.of(new AlertDTO(
                        temp.getDeviceId(),
                        AlertType.HIGH_TEMPERATURE,
                        AlertSeverity.CRITICAL,
                        System.currentTimeMillis(),
                        "Temperature exceeded 70°C: " + temp.getTemperature()
                ));
            }
        }

        if (data instanceof LightBulbData bulb) {
            if (bulb.getBrightness() != null && bulb.getBrightness() > 95) {
                return Optional.of(new AlertDTO(
                        bulb.getDeviceId(),
                        AlertType.HIGH_BRIGHTNESS,
                        AlertSeverity.WARNING,
                        System.currentTimeMillis(),
                        "Brightness is unusually high: " + bulb.getBrightness()
                ));
            }
        }

        return Optional.empty();
    }
}

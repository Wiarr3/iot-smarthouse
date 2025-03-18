package com.smartass.server.registry;

import com.smartass.server.model.device.DeviceData;
import com.smartass.server.model.device.LightBulbData;
import com.smartass.server.model.device.TemperatureSensorData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeviceDeserializerRegistry {
    private final Map<String, Class<? extends DeviceData>> registry = new HashMap<>();

    public DeviceDeserializerRegistry() {
        registry.put("temperature", TemperatureSensorData.class);
        registry.put("light", LightBulbData.class);
    }

    public Class<? extends DeviceData> resolve(String type) {
        return registry.get(type);
    }
}
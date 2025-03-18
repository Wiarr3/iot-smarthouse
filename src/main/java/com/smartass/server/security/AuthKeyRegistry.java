package com.smartass.server.security;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthKeyRegistry {
    private final Map<String, String> authKeys = new HashMap<String, String>();

    public AuthKeyRegistry() {
        authKeys.put("admin", "admin");
        authKeys.put("sensor-001", "key123");
        authKeys.put("light-001", "key456");
    }

    public boolean isValid(String deviceId, String authKey) {
        return authKey != null && authKey.equals(authKeys.get(deviceId));
    }
}

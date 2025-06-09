package com.smartass.server.config;

import com.smartass.server.websocket.AuthenticatedWebSocketHandler;
import com.smartass.server.websocket.DeviceCommandWebSocketHandler;
import com.smartass.server.websocket.DeviceTelemetryWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfig {
    @Bean
    public HandlerMapping webSocketMapping(DeviceCommandWebSocketHandler handler, ReactiveJwtDecoder jwtDecoder) {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/ws/commands", new AuthenticatedWebSocketHandler(handler, jwtDecoder));
        return new SimpleUrlHandlerMapping(map, 10);
    }

    @Bean
    public HandlerMapping telemetryWebSocketMapping(DeviceTelemetryWebSocketHandler telemetryHandler, ReactiveJwtDecoder jwtDecoder) {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/ws/telemetry", new AuthenticatedWebSocketHandler(telemetryHandler, jwtDecoder));
        return new SimpleUrlHandlerMapping(map, 20);
    }


    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

}

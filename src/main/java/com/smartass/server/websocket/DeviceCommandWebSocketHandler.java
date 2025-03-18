package com.smartass.server.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartass.server.service.dispatch.DeviceCommandDispatcher;
import com.smartass.server.model.command.DeviceCommandDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class DeviceCommandWebSocketHandler implements WebSocketHandler {

    private final DeviceCommandDispatcher dispatcher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(json -> {
                    try {
                        DeviceCommandDTO command = objectMapper.readValue(json, DeviceCommandDTO.class);
                        return dispatcher.dispatchCommand(command);
                    } catch (Exception e) {
                        System.err.println("Invalid command format: " + json);
                        return Mono.empty();
                    }
                })
                .then();
    }
}

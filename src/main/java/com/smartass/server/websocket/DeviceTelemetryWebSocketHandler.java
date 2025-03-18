package com.smartass.server.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class DeviceTelemetryWebSocketHandler implements WebSocketHandler {

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        sessions.add(session);
        return session.receive().then()
                .doFinally(signal -> sessions.remove(session));
    }

    public void broadcast(String message) {
        Flux.fromIterable(sessions)
                .flatMap(session -> session.send(
                        Mono.just(session.textMessage(message))
                ))
                .subscribe();
    }

    public void broadcastTelemetry(String json) {
        Flux.fromIterable(sessions)
                .flatMap(session -> session.send(Mono.just(session.textMessage("[telemetry]" + json))))
                .subscribe();
    }

    public void broadcastAlert(String json) {
        Flux.fromIterable(sessions)
                .flatMap(session -> session.send(Mono.just(session.textMessage("[alert]" + json))))
                .subscribe();
    }
}

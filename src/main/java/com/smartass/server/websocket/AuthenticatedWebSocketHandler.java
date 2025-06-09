package com.smartass.server.websocket;

import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.reactive.socket.*;
import reactor.core.publisher.Mono;

import java.util.List;

public class AuthenticatedWebSocketHandler implements WebSocketHandler {

    private final WebSocketHandler delegate;
    private final ReactiveJwtDecoder jwtDecoder;

    public AuthenticatedWebSocketHandler(WebSocketHandler delegate, ReactiveJwtDecoder jwtDecoder) {
        this.delegate = delegate;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String query = session.getHandshakeInfo().getUri().getQuery();
        if (query == null || !query.startsWith("token=")) {
            System.err.println("XXX No token provided in WebSocket connection.");
            return session.close(CloseStatus.BAD_DATA);
        }

        String token = query.substring("token=".length());

        return jwtDecoder.decode(token)
                .flatMap(jwt -> {
                    String subject = jwt.getSubject();
                    System.out.println("VVV WebSocket connection authorized for user: " + subject);
                    return delegate.handle(session);
                })
                .onErrorResume(e -> {
                    System.err.println("XXX WebSocket authentication failed: " + e.getMessage());
                    return session.close(CloseStatus.BAD_DATA);
                });
    }
}

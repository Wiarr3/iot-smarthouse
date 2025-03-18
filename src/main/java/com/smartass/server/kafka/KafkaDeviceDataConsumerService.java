package com.smartass.server.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartass.server.model.device.DeviceData;
import com.smartass.server.model.ErrorReason;
import com.smartass.server.security.AuthKeyRegistry;
import com.smartass.server.service.processing.DeviceProcessingService;
import com.smartass.server.websocket.DeviceTelemetryWebSocketHandler;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

@Service
public class KafkaDeviceDataConsumerService {
    private static final Logger log = LoggerFactory.getLogger(KafkaDeviceDataConsumerService.class);

    private final KafkaReceiver<String, DeviceData> kafkaReceiver;
    private final AuthKeyRegistry authKeyRegistry;
    private final DeviceProcessingService processingService;
    private final MeterRegistry meterRegistry;
    private final DeviceTelemetryWebSocketHandler telemetryWebSocketHandler;
    private final ObjectMapper objectMapper;

    public KafkaDeviceDataConsumerService(KafkaReceiver<String, DeviceData> kafkaReceiver,
                                          AuthKeyRegistry authKeyRegistry,
                                          DeviceProcessingService processingService,
                                          MeterRegistry meterRegistry,
                                          DeviceTelemetryWebSocketHandler telemetryWebSocketHandler) {
        this.kafkaReceiver = kafkaReceiver;
        this.authKeyRegistry = authKeyRegistry;
        this.processingService = processingService;
        this.meterRegistry = meterRegistry;
        this.telemetryWebSocketHandler = telemetryWebSocketHandler;
        this.objectMapper = new ObjectMapper();

        startReceiving();
    }

    private void startReceiving() {
        kafkaReceiver.receive()
                .doOnNext(this::processRecord)
                .subscribe();
    }

    private void processRecord(ReceiverRecord<String, DeviceData> record) {
        try {
            DeviceData data = record.value();

            if (!authKeyRegistry.isValid(data.getDeviceId(), data.getAuthKey())) {
                log.warn("[Kafka] Unauthorized device: {}", data.getDeviceId());
                meterRegistry.counter("iot.message.errors", "reason", ErrorReason.INVALID_AUTH.name()).increment();
                return;
            }

            processingService.handleGeneric(data);
            meterRegistry.counter("iot.message.success", "type", data.getType()).increment();

            try {
                String json = objectMapper.writeValueAsString(data);
                telemetryWebSocketHandler.broadcast(json);
            } catch (Exception ex) {
                log.warn("[WebSocket] Failed to send message to frontend", ex);
            }

        } catch (Exception e) {
            log.error("[Kafka] Exception during message processing", e);
            meterRegistry.counter("iot.message.errors", "reason", ErrorReason.PROCESSING_ERROR.name()).increment();
        } finally {
            record.receiverOffset().acknowledge();
        }
    }
}

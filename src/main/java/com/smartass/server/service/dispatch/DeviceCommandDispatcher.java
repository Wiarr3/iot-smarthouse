package com.smartass.server.service.dispatch;

import com.smartass.server.kafka.KafkaCommandProducerService;
import com.smartass.server.model.command.DeviceCommandDTO;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeviceCommandDispatcher {
    private final KafkaCommandProducerService kafkaProducer;
    private final MeterRegistry meterRegistry;

    public DeviceCommandDispatcher(final KafkaCommandProducerService kafkaProducer,
                                   final MeterRegistry meterRegistry) {
        this.kafkaProducer = kafkaProducer;
        this.meterRegistry = meterRegistry;
    }

    public Mono<Void> dispatchCommand(DeviceCommandDTO command) {
        if (command.getDeviceId() == null || command.getCommand() == null) {
            meterRegistry.counter("iot.command.dispatch.error", "reason", "invalid_command").increment();
            return Mono.error(new IllegalArgumentException("Invalid command: deviceId and command must not be null"));
        }

        return kafkaProducer.send(command)
                .doOnSuccess(v -> {
                    meterRegistry.counter("iot.command.dispatch.success", "command", command.getCommand()).increment();
                })
                .doOnError(e -> {
                    meterRegistry.counter("iot.command.dispatch.error", "reason", "producer_error", "command", command.getCommand()).increment();
                });
    }
}

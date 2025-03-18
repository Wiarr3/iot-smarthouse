package com.smartass.server.kafka;

import com.smartass.server.model.device.DeviceData;
import com.smartass.server.simulator.SimulatorProperties;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class KafkaDeviceDataProducerService {

    private final KafkaSender<String, DeviceData> kafkaSender;
    private final MeterRegistry meterRegistry;
    private final SimulatorProperties simulatorProperties;

    public KafkaDeviceDataProducerService(KafkaSender<String, DeviceData> kafkaSender,
                                          MeterRegistry meterRegistry, SimulatorProperties simulatorProperties) {
        this.kafkaSender = kafkaSender;
        this.meterRegistry = meterRegistry;
        this.simulatorProperties = simulatorProperties;
    }

    public Mono<Void> send(DeviceData data) {
        System.out.println("send try");
        SenderRecord<String, DeviceData, String> record =
                SenderRecord.create(new ProducerRecord<>("device-data", data.getDeviceId(), data), data.getDeviceId());

        Timer.Sample sample = Timer.start(meterRegistry);
        Flux<SenderResult<String>> resultFlux = kafkaSender.send(Mono.just(record));

        return resultFlux
                .doOnNext(result -> {
                    meterRegistry.counter("iot.telemetry.sent", "type", data.getType(), "deviceId", data.getDeviceId()).increment();
                    sample.stop(Timer.builder("iot.telemetry.send.time")
                            .description("Time of sending telemetry data from a device")
                            .tag("type", data.getType())
                            .register(meterRegistry));
                    System.out.println("Telemetry sent from device: " + data.getDeviceId());
                })
                .doOnError(error -> {
                    meterRegistry.counter("iot.telemetry.error", "type", data.getType()).increment();
                    System.err.println("Telemetry send error: " + error.getMessage());
                })
                .retryWhen(Retry.fixedDelay(simulatorProperties.getRetries(), simulatorProperties.getDelay()))
                .then();
    }
}

package com.smartass.server.kafka;
import com.smartass.server.model.command.DeviceCommandDTO;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.SenderResult;


@Service
public class KafkaCommandProducerService {
    private final KafkaSender<String, DeviceCommandDTO> kafkaSender;
    private final MeterRegistry meterRegistry;

    public KafkaCommandProducerService(KafkaSender<String, DeviceCommandDTO> kafkaSender
    , MeterRegistry meterRegistry) {
        this.kafkaSender = kafkaSender;
        this.meterRegistry = meterRegistry;
    }

    public Mono<Void> send(DeviceCommandDTO command) {
        SenderRecord<String, DeviceCommandDTO, String> record =
                SenderRecord.create(new ProducerRecord<>("device-control", command.getDeviceId(), command), command.getDeviceId());
        Timer.Sample sample = Timer.start(meterRegistry);
        Flux<SenderResult<String>> resultFlux = kafkaSender.send(Mono.just(record));


        return resultFlux
                .doOnNext(result -> {
                    meterRegistry.counter("iot.command.sent", "command", command.getCommand(), "deviceId", command.getDeviceId()).increment();
                    sample.stop(Timer.builder("iot.command.send.time")
                            .description("Time of sending command to a device")
                            .tag("command", command.getCommand())
                            .register(meterRegistry));
                    System.out.println("Sent command to device: " + command.getDeviceId());
                })
                .doOnError(error -> {
                    meterRegistry.counter("iot.command.error", "command", command.getCommand()).increment();
                    System.err.println("Failed to send command: " + command.getCommand());
                })
                .then();

    }
}

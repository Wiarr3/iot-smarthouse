package com.smartass.server.simulator;


import com.smartass.server.kafka.KafkaDeviceDataProducerService;
import com.smartass.server.model.device.DeviceData;
import com.smartass.server.model.device.TemperatureSensorData;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.time.Duration;

@Component
@Profile("simulator")
public class TemperatureSensorSimulator implements Simulator {

    private final KafkaDeviceDataProducerService kafkaProducerService;
    public TemperatureSensorSimulator(KafkaDeviceDataProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public void simulate() {
        Flux.interval(Duration.ofSeconds(15))
                .flatMap(tick -> {
                    TemperatureSensorData data = TemperatureSensorData.builder()
                            .deviceId("sensor-001")
                            .type("temperature")
                            .timestamp(System.currentTimeMillis())
                            .temperature(20 + Math.random() * 10)
                            .humidity(30 + Math.random() * 20)
                            .authKey("key123")
                            .build();

                    return kafkaProducerService.send(data);
                })
                .subscribe();
    }
}

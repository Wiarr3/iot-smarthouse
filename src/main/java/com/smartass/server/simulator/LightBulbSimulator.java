package com.smartass.server.simulator;


import com.smartass.server.kafka.KafkaDeviceDataProducerService;
import com.smartass.server.model.device.DeviceData;
import com.smartass.server.model.device.LightBulbData;
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
public class LightBulbSimulator implements Simulator {

    private final KafkaDeviceDataProducerService kafkaProducerService;

    public LightBulbSimulator(KafkaDeviceDataProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public void simulate() {
        Flux.interval(Duration.ofSeconds(10))
                .flatMap(tick -> {
                    LightBulbData data = LightBulbData.builder()
                            .deviceId("light-001")
                            .type("light")
                            .timestamp(System.currentTimeMillis())
                            .state(Math.random() > 0.5)
                            .brightness((int) (Math.random() * 100))
                            .authKey("key456")
                            .build();

                    return kafkaProducerService.send(data);
                })
                .subscribe();
    }
}

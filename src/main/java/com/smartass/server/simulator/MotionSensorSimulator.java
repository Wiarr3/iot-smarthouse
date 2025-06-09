package com.smartass.server.simulator;

import com.smartass.server.kafka.KafkaDeviceDataProducerService;
import com.smartass.server.model.device.MotionSensorData;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Random;

@Component
@Profile("simulator")
public class MotionSensorSimulator implements Simulator {

    private final KafkaDeviceDataProducerService kafkaProducerService;
    private final Random random = new Random();
    private int burglaryDuration = 0;
    private final double burglaryChance = 0.0001;
    private boolean burglaryDetected = false;
    private int maxburglaryDuration = 0;

    public MotionSensorSimulator(KafkaDeviceDataProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public void simulate() {
        Flux.interval(Duration.ofSeconds(5))
                .flatMap(tick -> {
                    long timestamp = Instant.now().toEpochMilli();
                    LocalTime now = LocalTime.now();
                    boolean motionDetected = false;

                    double lambda;
                    if (now.isAfter(LocalTime.of(6, 0)) && now.isBefore(LocalTime.of(7, 0))) {
                        lambda = 0.09;
                    }
                    else if (now.isAfter(LocalTime.of(7, 0)) && now.isBefore(LocalTime.of(14, 0))){
                        lambda = 0.005;
                    }
                    else if (now.isAfter(LocalTime.of(14, 0)) && now.isBefore(LocalTime.of(23, 0))){
                        lambda = 0.07;
                    }
                    else {
                        lambda = 0.0001;
                        if (!burglaryDetected && Math.random() < burglaryChance) {
                            burglaryDetected = true;
                            burglaryDuration = 0;
                            maxburglaryDuration = 12*20 + random.nextInt(120);
                        }
                        else if (burglaryDetected && burglaryDuration < maxburglaryDuration) {
                            burglaryDuration++;
                            lambda = 0.1;
                        }
                    }

                    double p = random.nextDouble();
                    if (p < lambda) {
                        motionDetected = true;
                    }

                    MotionSensorData data = MotionSensorData.builder()
                            .deviceId("motion-001")
                            .type("motion")
                            .timestamp(timestamp)
                            .motionDetected(motionDetected)
                            .authKey("key745")
                            .build();

                    return kafkaProducerService.send(data);
                })
                .subscribe();
    }
}
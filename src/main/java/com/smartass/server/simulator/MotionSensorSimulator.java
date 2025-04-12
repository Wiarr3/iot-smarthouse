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
    private double batteryLevel = 100.0;
    private final double batteryDrainPerEvent = 0.01; // rozładowanie na każde wykrycie ruchu

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

                    // Określenie intensywności λ w zależności od pory dnia
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
                    }

                    // Generowanie zdarzenia ruchu zgodnie z rozkładem Poissona
                    double p = random.nextDouble();
                    if (p < lambda) {
                        motionDetected = true;
                    }

                    // Przygotowanie danych
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
package com.smartass.server.simulator;

import com.smartass.server.kafka.KafkaDeviceDataProducerService;
import com.smartass.server.model.device.EnergyMeterData;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

@Component
@Profile("simulator")
public class EnergyMeterSimulator implements Simulator {

    private final KafkaDeviceDataProducerService kafkaProducerService;
    private final Random random = new Random();
    private double currentPower;
    private double totalEnergy;
    private int currentState;
    private int stateDuration = 0;

    public EnergyMeterSimulator(KafkaDeviceDataProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
        this.currentPower = 20.0 + random.nextDouble() * 10;
        this.totalEnergy = 0.0;
    }

    @Override
    public void simulate() {
        Flux.interval(Duration.ofSeconds(5))
                .flatMap(tick -> {
                    long timestamp = Instant.now().toEpochMilli();

                    stateDuration++;
                    if (stateDuration >= 60 + random.nextInt(60)) {
                        currentState = random.nextInt(4);
                        stateDuration = 0;
                    }

                    switch (currentState) {
                        case 0:
                            currentPower = 3.0 + random.nextDouble() * 2;
                            break;
                        case 1:
                            currentPower = 50.0 + random.nextDouble() * 50;
                            break;
                        case 2:
                            currentPower = 100.0 + random.nextDouble() * 50;
                            break;
                        case 3:
                            currentPower = 200.0 + random.nextDouble() * 200;
                            break;
                    }

                    totalEnergy += (currentPower / 1000) * (5.0 / 3600); // kWh = (W / 1000) * (czas w godzinach)


                    EnergyMeterData data = EnergyMeterData.builder()
                            .deviceId("energy-001")
                            .type("energy")
                            .timestamp(timestamp)
                            .currentPower(currentPower) // aktualna moc (W)
                            .totalEnergy(totalEnergy) // całkowita energia (kWh)
                            .authKey("key412")
                            .build();

                    return kafkaProducerService.send(data);
                })
                .subscribe();
    }
}
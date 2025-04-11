package com.smartass.server.simulator;

import com.smartass.server.kafka.KafkaDeviceDataProducerService;
import com.smartass.server.model.device.SmokeDetectorData;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

@Component
@Profile("simulator")
public class SmokeDetectorSimulator implements Simulator {

    private final KafkaDeviceDataProducerService kafkaProducerService;
    private final Random random = new Random();
    private boolean smokeDetected = false;
    private double battery;
    private final double batteryDrain = 0.005;
    private int smokeEventDuration = 0;
    private double smokeLevel = 0.0;
    private final double anomalyChance = 0.005; // 0.5% szansy na wykrycie dymu
    private final double falseAlarmChance = 0.001; // 0.1% szansy na fałszywy alarm

    public SmokeDetectorSimulator(KafkaDeviceDataProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
        this.battery = 80.0 + (new Random().nextDouble() * 20);
    }

    @Override
    public void simulate() {
        Flux.interval(Duration.ofSeconds(30)) // Sprawdzanie co 30 sekund
                .flatMap(tick -> {
                    long timestamp = Instant.now().toEpochMilli();

                    // Symulacja normalnego działania
                    if (!smokeDetected) {
                        smokeLevel = random.nextDouble() * 0.1; // Normalny poziom dymu < 0.1
                        battery -= batteryDrain * (1+random.nextDouble()/10);
                        // Sprawdź czy wystąpi zdarzenie dymu
                        if (random.nextDouble() < anomalyChance) {
                            smokeDetected = true;
                            smokeEventDuration = 0;
                        }
                        // Sprawdź czy wystąpi fałszywy alarm
                        else if (random.nextDouble() < falseAlarmChance) {
                            smokeDetected = true;
                            smokeEventDuration = 0;
                        }
                    }
                    // Symulacja zdarzenia dymu
                    else {
                        smokeEventDuration++;
                        battery += batteryDrain * (1+random.nextDouble());
                        // Zwiększ poziom dymu na początku zdarzenia
                        if (smokeEventDuration < 10) {
                            smokeLevel = Math.min(1.0, smokeLevel + 0.1 + random.nextDouble() * 0.2);
                        }
                        // Po pewnym czasie zacznij zmniejszać poziom dymu
                        else if (smokeEventDuration > 30) {
                            smokeLevel = Math.max(0.0, smokeLevel - 0.05 - random.nextDouble() * 0.1);
                        }

                        // Zakończ zdarzenie gdy poziom dymu spadnie poniżej progu
                        if (smokeLevel < 0.05) {
                            smokeDetected = false;
                        }
                    }

                    SmokeDetectorData data = SmokeDetectorData.builder()
                            .deviceId("smoke-001")
                            .type("smoke")
                            .timestamp(timestamp)
                            .smokeLevel(smokeLevel)
                            .alarmActive(smokeDetected)
                            .batteryLevel(battery)
                            .authKey("key835")
                            .build();

                    return kafkaProducerService.send(data);
                })
                .subscribe();
    }
}

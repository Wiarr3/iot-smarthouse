//package com.smartass.server.simulator;
//
//import com.smartass.server.kafka.KafkaDeviceDataProducerService;
//import com.smartass.server.model.device.SmokeDetectorData;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Flux;
//
//import java.time.Duration;
//import java.time.Instant;
//import java.util.Random;
//
//@Component
//@Profile("simulator")
//public class SmokeDetectorSimulator implements Simulator {
//
//    private final KafkaDeviceDataProducerService kafkaProducerService;
//    private final Random random = new Random();
//    private boolean smokeDetected = false;
//    private boolean CigaretteDetected = false;
//    private boolean FireDetected = false;
//    private double battery;
//    private final double batteryDrain = 0.005;
//    private int smokeEventDuration = 0;
//    private double smokeLevel = 0.0;
//    private final double CigaretteChance = 0.005; // 0.5% szansy na wykrycie dymu
//    private final double FireChance = 0.001; // 0.1% szansy na fałszywy alarm
//    private double Chance = 0.0;
//    private int batteryEventDuration = 0;
//    private int batteryEventDurationEND = 0;
//
//    public SmokeDetectorSimulator(KafkaDeviceDataProducerService kafkaProducerService) {
//        this.kafkaProducerService = kafkaProducerService;
//        this.battery = 80.0 + (new Random().nextDouble() * 20);
//    }
//
//    @Override
//    public void simulate() {
//        Flux.interval(Duration.ofSeconds(30)) // Sprawdzanie co 30 sekund
//                .flatMap(tick -> {
//                    long timestamp = Instant.now().toEpochMilli();
//
//                    if (!smokeDetected && !FireDetected && !CigaretteDetected) {
//                        Chance = Math.random();
//                        battery -= batteryDrain * (1+random.nextDouble()/10);
//                        if (Chance < FireChance) {
//                            FireDetected = true;
//                            smokeEventDuration = 0;
//                        }
//                        else if (Chance < CigaretteChance) {
//                            CigaretteDetected = true;
//                            smokeEventDuration = 0;
//                        }
//                        else {
//                            smokeLevel = random.nextDouble() * 0.1;
//                        }
//                    }
//
//                    else {
//                        smokeEventDuration++;
//                        // Zwiększ poziom dymu na początku zdarzenia
//                        if (FireDetected && smokeEventDuration <= 60) {
//                            smokeLevel = Math.min(1.0, smokeLevel + 0.1 + random.nextDouble() * 0.2);
//                        }
//                        // Po pewnym czasie zacznij zmniejszać poziom dymu
//                        else if (CigaretteDetected && smokeEventDuration <= 10) {
//                            smokeLevel = Math.max(0.5 + random.nextDouble() * 0.05, smokeLevel + 0.02 + random.nextDouble() * 0.01);
//                        }
//
//                        // Zakończ zdarzenie gdy poziom dymu spadnie poniżej progu
//                        if (smokeLevel > 0.2) {
//                            smokeDetected = true;
//                            battery -= batteryDrain * (1+random.nextDouble());
//                        }
//
//                        else if (smokeLevel < 0.2) {
//                            smokeDetected = false;
//                            battery -= batteryDrain * (1+random.nextDouble()/10);
//                        }
//                    }
//                    if (battery <= 0) {
//                        if (batteryEventDuration == 0) {
//                            batteryEventDurationEND = 120 + random.nextInt(120);
//                        }
//                        smokeLevel = Double.NaN;
//                        smokeDetected = false;
//                        battery = 0;
//                        batteryEventDuration += 1;
//
//                        if (batteryEventDuration >= batteryEventDurationEND) {
//                            battery = 80.0 + (random.nextDouble() * 20);
//                            batteryEventDuration = 0;
//                            batteryEventDurationEND = 0;
//                        }
//
//                    }
//
//                    SmokeDetectorData data = SmokeDetectorData.builder()
//                            .deviceId("smoke-001")
//                            .type("smoke")
//                            .timestamp(timestamp)
//                            .smokeLevel(smokeLevel)
//                            .alarmActive(smokeDetected)
//                            .batteryLevel(battery)
//                            .authKey("key835")
//                            .build();
//
//                    return kafkaProducerService.send(data);
//                })
//                .subscribe();
//    }
//}

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
    private boolean cigaretteDetected = false;
    private boolean fireDetected = false;
    private double battery;
    private final double batteryDrain = 0.005;
    private int smokeEventDuration = 0;
    private double smokeLevel = 0.01;
    private final double cigaretteChance = 0.005; // 0.5% szansy na wykrycie dymu
    private final double fireChance = 0.0001; // 0.01% szansy na fałszywy alarm
    private double chance = 0.0;
    private int batteryEventDuration = 0;
    private int batteryEventDurationEND = 0;
    private int maxFireDuration = 120; // Maksymalny czas trwania pożaru (w cyklach)
    private int maxCigaretteDuration = 30; // Maksymalny czas trwania papierosa (w cyklach)
    private boolean isEventEnding = false;

    public SmokeDetectorSimulator(KafkaDeviceDataProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
        this.battery = 80.0 + (new Random().nextDouble() * 20);
    }

    @Override
    public void simulate() {
        Flux.interval(Duration.ofSeconds(30)) // Sprawdzanie co 30 sekund
                .flatMap(tick -> {
                    long timestamp = Instant.now().toEpochMilli();

                    if (!smokeDetected && !fireDetected && !cigaretteDetected && !isEventEnding) {
                        chance = Math.random();
                        battery -= batteryDrain * (1 + random.nextDouble() / 10);
                        if (chance < fireChance) {
                            fireDetected = true;
                            smokeEventDuration = 0;
                            maxFireDuration = 30 + random.nextInt(30);
                        }
                        else if (chance < cigaretteChance) {
                            cigaretteDetected = true;
                            smokeEventDuration = 0;
                            maxCigaretteDuration = 7 + random.nextInt(7);
                        }
                        else {
                            smokeLevel = random.nextDouble() * 0.1;
                        }
                    }
                    else {
                        smokeEventDuration++;

                        // Obsługa trwającego zdarzenia
                        if (fireDetected) {
                            if (smokeEventDuration <= maxFireDuration) {
                                // Faza narastania dymu
                                smokeLevel = Math.min(0.9 + random.nextDouble()*0.1 , smokeLevel + 0.07 + random.nextDouble() * 0.01);
                            }
                            else if (smokeEventDuration > maxFireDuration && smokeLevel > 0.1) {
                                // Faza zmniejszania dymu
                                smokeLevel = Math.max(0.0 + random.nextDouble()*0.1, smokeLevel - (0.03 + random.nextDouble() * 0.01));
                                isEventEnding = true;
                            }
                            else {
                                // Zakończenie zdarzenia
                                fireDetected = false;
                                isEventEnding = false;
                                smokeLevel = random.nextDouble() * 0.1;
                            }
                        }
                        else if (cigaretteDetected) {
                            if (smokeEventDuration <= maxCigaretteDuration) {
                                // Faza narastania dymu
                                smokeLevel = Math.min(0.5 + random.nextDouble()*0.1, smokeLevel + 0.05 + random.nextDouble() * 0.01);
                            }
                            else if (smokeEventDuration > maxCigaretteDuration && smokeLevel > 0.1) {
                                // Faza zmniejszania dymu
                                smokeLevel = Math.max(0.0 + random.nextDouble()*0.1, smokeLevel - (0.03 + random.nextDouble() * 0.01));
                                isEventEnding = true;
                            }
                            else {
                                // Zakończenie zdarzenia
                                cigaretteDetected = false;
                                isEventEnding = false;
                                smokeLevel = random.nextDouble() * 0.1;
                            }
                        }

                        // Ustawienie flagi smokeDetected na podstawie poziomu dymu
                        if (smokeLevel > 0.2) {
                            smokeDetected = true;
                            battery -= batteryDrain * (1 + random.nextDouble());
                        }
                        else {
                            smokeDetected = false;
                            battery -= batteryDrain * (1 + random.nextDouble() / 10);
                        }
                    }

                    if (battery <= 0) {
                        if (batteryEventDuration == 0) {
                            batteryEventDurationEND = 120 + random.nextInt(120);
                        }
                        smokeLevel = Double.NaN;
                        smokeDetected = false;
                        fireDetected = false;
                        cigaretteDetected = false;
                        isEventEnding = false;
                        battery = 0;
                        batteryEventDuration += 1;

                        if (batteryEventDuration >= batteryEventDurationEND) {
                            battery = 80.0 + (random.nextDouble() * 20);
                            batteryEventDuration = 0;
                            batteryEventDurationEND = 0;
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
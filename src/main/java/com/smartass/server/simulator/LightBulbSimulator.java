package com.smartass.server.simulator;

import com.smartass.server.kafka.KafkaDeviceDataProducerService;
import com.smartass.server.model.device.LightBulbData;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;

@Component
@Profile("simulator")
public class LightBulbSimulator implements Simulator {

    private final KafkaDeviceDataProducerService kafkaProducerService;
    private boolean isOn = false;  // Czy żarówka jest włączona?
    private int brightness = 0;    // Jasność 0-100
    private int activeTime = 0;    // Jak długo światło było włączone

    public LightBulbSimulator(KafkaDeviceDataProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public void simulate() {
        Flux.interval(Duration.ofSeconds(10))
                .flatMap(tick -> {
                    long timestamp = Instant.now().toEpochMilli();
                    int hour = (int) ((timestamp / (1000 * 60 * 60)) % 24);  // Aktualna godzina (0-23)

                    // Wpływ pory dnia
                    boolean shouldBeOn = (hour >= 18 || hour < 2);  // Światło głównie wieczorem i w nocy

                    // Decyzja o zmianie stanu
                    if (!isOn && shouldBeOn && Math.random() < 0.7) {
                        isOn = true;
                        brightness = 50 + (int) (Math.random() * 50);  // Jasność 50-100%
                        activeTime = 0;
                    } else if (isOn) {
                        activeTime++;

                        // Stopniowe zmiany jasności
                        if (Math.random() < 0.3) {
                            brightness += (Math.random() > 0.5) ? 5 : -5;
                            brightness = Math.max(10, Math.min(100, brightness));  // Ograniczenie do 10-100%
                        }

                        // Wyłączanie po dłuższym czasie
                        if (Math.random() < 0.2 || activeTime > 30) {
                            isOn = false;
                            brightness = 0;
                        }
                    }

                    // Tworzenie danych
                    LightBulbData data = LightBulbData.builder()
                            .deviceId("light-001")
                            .type("light")
                            .timestamp(timestamp)
                            .state(isOn)
                            .brightness(brightness)
                            .authKey("key456")
                            .build();

                    return kafkaProducerService.send(data);
                })
                .subscribe();
    }
}


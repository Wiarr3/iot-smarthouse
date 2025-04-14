package com.smartass.server.simulator;

import com.smartass.server.kafka.KafkaDeviceDataProducerService;
import com.smartass.server.model.device.FridgeTemperatureSensorData;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

@Component
@Profile("simulator")
public class FridgeTemperatureSensorSimulator implements Simulator {

    private final KafkaDeviceDataProducerService kafkaProducerService;
    private final Random random = new Random();

    private final double targetTemperature = 6.0;
    private final double temperatureDrift = 0.001;
    private final double doorOpenChance = 0.01;
    private final double powerOutageChance = 0.00005;

    private boolean doorOpen = false;
    private int doorOpenTime = 0;
    private boolean powerOutage = false;
    private int powerOutageTime = 0;
    private double simulatedTemperature = targetTemperature;
    private boolean cooling = false;

    private static final double DOOR_OPEN_TEMP_INCREASE = 0.2;
    private static final double POWER_OUTAGE_TEMP_INCREASE = 0.05;
    private static final double COOLING_RATE = 0.1;

    private int MAX_DOOR_OPEN_TIME = 10;
    private int MAX_POWER_OUTAGE_TIME = 40;

    public FridgeTemperatureSensorSimulator(KafkaDeviceDataProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public void simulate() {
        Flux.interval(Duration.ofSeconds(15))
                .flatMap(tick -> {
                    long timestamp = Instant.now().toEpochMilli();

                    double compressorCycle = (timestamp / (1000.0 * 60 * 20)) % 1;
                    double temperatureVariation = Math.sin(compressorCycle * Math.PI * 2);

                    // Symulacja normalnej pracy lodówki
                    if (!powerOutage && !doorOpen && !cooling) {
                        // Losowe otwarcie drzwi
                        if (random.nextDouble() < doorOpenChance) {
                            doorOpen = true;
                            doorOpenTime = 0;
                            MAX_DOOR_OPEN_TIME = MAX_DOOR_OPEN_TIME + random.nextInt(MAX_DOOR_OPEN_TIME);
                        }
                        // Zanik prądu
                        else if (random.nextDouble() < powerOutageChance) {
                            powerOutage = true;
                            powerOutageTime = 0;
                            MAX_POWER_OUTAGE_TIME = MAX_POWER_OUTAGE_TIME + random.nextInt(MAX_POWER_OUTAGE_TIME);
                        }
                        // Normalna praca - utrzymanie temperatury
                        else {
                            // Małe wahania wokół temperatury docelowej
                            simulatedTemperature = targetTemperature + (random.nextDouble() - 0.5) * temperatureDrift + temperatureVariation;
                        }
                    }

                    // Obsługa otwartych drzwi
                    else if (doorOpen) {
                        simulatedTemperature += DOOR_OPEN_TEMP_INCREASE + (random.nextDouble() - 0.5) * 0.05;
                        doorOpenTime++;

                        // Zamknięcie drzwi po pewnym czasie
                        if (doorOpenTime >= MAX_DOOR_OPEN_TIME || random.nextDouble() < 0.1) {
                            doorOpen = false;
                            cooling = true;
                        }
                    }

                    // Obsługa zaniku prądu
                    else if (powerOutage) {
                        simulatedTemperature += POWER_OUTAGE_TEMP_INCREASE + (random.nextDouble() - 0.5) * 0.01;
                        powerOutageTime++;

                        // Przywrócenie zasilania
                        if (powerOutageTime >= MAX_POWER_OUTAGE_TIME || random.nextDouble() < 0.02) {
                            powerOutage = false;
                            cooling = true;
                        }
                    }

                    // Powrót do temperatury docelowej po zdarzeniach
                    if (cooling) {
                        simulatedTemperature = simulatedTemperature - COOLING_RATE + (random.nextDouble() - 0.5)*0.05;
                        if (simulatedTemperature < targetTemperature + temperatureVariation) {
                            cooling = false;
                        }
                    }


                    FridgeTemperatureSensorData data = FridgeTemperatureSensorData.builder()
                            .deviceId("fridge-001")
                            .type("fridge")
                            .timestamp(timestamp)
                            .temperature(simulatedTemperature)
                            .doorOpen(doorOpen)
                            .authKey("key997")
                            .build();

                    return kafkaProducerService.send(data);
                })
                .subscribe();
    }
}
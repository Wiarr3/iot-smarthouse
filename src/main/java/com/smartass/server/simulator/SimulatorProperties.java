package com.smartass.server.simulator;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "simulator")
public class SimulatorProperties {

    private Lightbulb lightbulb = new Lightbulb();
    private Temperature temperature = new Temperature();

    private int retries;
    private Duration delay;

    @Getter
    @Setter
    public static class Lightbulb {
        private boolean enabled;
    }

    @Getter
    @Setter
    public static class Temperature {
        private boolean enabled;
    }
}

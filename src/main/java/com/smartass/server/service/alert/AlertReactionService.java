package com.smartass.server.service.alert;

import com.smartass.server.model.device.DeviceData;
import com.smartass.server.model.alert.AlertDTO;
import com.smartass.server.service.alert.notifiers.AlertNotifier;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class AlertReactionService {

    private final AlertRuleEngine ruleEngine;
    private final MeterRegistry meterRegistry;
    private final List<AlertNotifier> notifiers;

    public AlertReactionService(AlertRuleEngine ruleEngine,
                                MeterRegistry meterRegistry,
                                List<AlertNotifier> notifiers) {
        this.ruleEngine = ruleEngine;
        this.meterRegistry = meterRegistry;
        this.notifiers = notifiers;
    }

    public List<AlertDTO> evaluateAndReact(DeviceData data) {
        List<AlertDTO> alertOpt = ruleEngine.evaluateAll(data);

        alertOpt.forEach(alert -> {
            meterRegistry.counter("iot.alerts.triggered",
                    "type", alert.getType().name(),
                    "severity", alert.getSeverity().name()
            ).increment();

            notifiers.forEach(notifier -> notifier.notify(alert));
        });

        return alertOpt;
    }
}

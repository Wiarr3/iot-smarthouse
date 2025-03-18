package com.smartass.server.service.alert.notifiers;

import com.smartass.server.model.alert.AlertDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "notifier.log", name = "enabled", havingValue = "true")
public class LogAlertNotifier implements AlertNotifier {

    private static final Logger log = LoggerFactory.getLogger(LogAlertNotifier.class);

    @Override
    public void notify(AlertDTO alert) {
        log.warn("[ALERT LOGGED] Device: {}, Type: {}, Severity: {}, Desc: {}",
                alert.getDeviceId(), alert.getType(), alert.getSeverity(), alert.getDescription());
    }
}

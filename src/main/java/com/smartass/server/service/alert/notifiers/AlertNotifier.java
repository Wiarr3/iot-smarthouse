package com.smartass.server.service.alert.notifiers;

import com.smartass.server.model.alert.AlertDTO;

public interface AlertNotifier {
    public void notify(AlertDTO message);
}

package com.smartass.server.repository;

import com.smartass.server.model.entity.TemperatureSensorDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemperatureSensorDataRepository extends JpaRepository<TemperatureSensorDataEntity, Long> {
}

package com.smartass.server.repository;

import com.smartass.server.model.entity.FridgeTemperatureSensorDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FridgeTemperatureSensorRepository extends JpaRepository<FridgeTemperatureSensorDataEntity, Long> {
}

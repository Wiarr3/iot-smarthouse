package com.smartass.server.repository;

import com.smartass.server.model.entity.MotionSensorDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotionSensorDataRepository extends JpaRepository<MotionSensorDataEntity, Long> {
}

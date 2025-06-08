package com.smartass.server.repository;

import com.smartass.server.model.entity.EnergyMeterDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnergyMeterDataRepository extends JpaRepository<EnergyMeterDataEntity, Long> {
}

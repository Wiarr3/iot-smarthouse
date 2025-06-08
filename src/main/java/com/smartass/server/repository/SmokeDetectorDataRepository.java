package com.smartass.server.repository;

import com.smartass.server.model.entity.SmokeDetectorDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmokeDetectorDataRepository extends JpaRepository<SmokeDetectorDataEntity, Long> {
}

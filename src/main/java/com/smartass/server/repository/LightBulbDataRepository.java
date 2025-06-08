package com.smartass.server.repository;

import com.smartass.server.model.entity.LightBulbDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LightBulbDataRepository extends JpaRepository<LightBulbDataEntity, Long> {
}

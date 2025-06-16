package com.smartass.server.repository;

import com.smartass.server.model.alert.AlertEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertEventRepository extends JpaRepository<AlertEventEntity, Long> {
}

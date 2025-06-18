package com.smartass.server.repository;

import com.smartass.server.model.alert.AlertCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertConditionRepository extends JpaRepository<AlertCondition, Long> {
}

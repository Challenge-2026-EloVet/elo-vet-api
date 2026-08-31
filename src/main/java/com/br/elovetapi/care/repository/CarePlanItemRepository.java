package com.br.elovetapi.care.repository;

import com.br.elovetapi.care.model.CarePlanItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarePlanItemRepository extends JpaRepository<CarePlanItem, Long> {
    List<CarePlanItem> findByCarePlanId(Long carePlanId);
}
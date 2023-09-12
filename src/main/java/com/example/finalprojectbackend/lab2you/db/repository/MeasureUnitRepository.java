package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.MeasureUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasureUnitRepository extends JpaRepository<MeasureUnitEntity, Long> {
    List<MeasureUnitEntity> findAllByIsDeletedFalse();
}

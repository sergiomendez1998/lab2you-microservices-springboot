package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.ExamTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamTypeRepository extends JpaRepository<ExamTypeEntity, Long> {
    List<ExamTypeEntity> findAllByIsDeletedFalse();
}

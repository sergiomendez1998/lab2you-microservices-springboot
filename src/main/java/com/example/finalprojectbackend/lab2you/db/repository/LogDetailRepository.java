package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.LogDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogDetailRepository extends JpaRepository<LogDetail, Long> {
}

package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.LogHeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogHeaderRepository extends JpaRepository<LogHeader, Long> {
}

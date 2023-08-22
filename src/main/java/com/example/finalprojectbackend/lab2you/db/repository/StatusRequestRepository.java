package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.StatusRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRequestRepository extends JpaRepository<StatusRequest, Long> {
}

package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    Employee findByName(String name);
}

package com.example.finalprojectbackend.lab2you.db.model.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name = "assignments")
public class AssignmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assigned_by")
    private EmployeeEntity assignedByEmployee;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private EmployeeEntity assignedToEmployee;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private RequestEntity request;
    private boolean isCurrentAssignment;
    private LocalDate assignationDate;

    @PrePersist
    public void prePersist() {
        this.isCurrentAssignment = true;
    }

}
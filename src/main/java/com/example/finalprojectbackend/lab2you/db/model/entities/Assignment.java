package com.example.finalprojectbackend.lab2you.db.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name = "assignments")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assigned_by")
    private Employee assignedByEmployee;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private Employee assignedToEmployee;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;
}

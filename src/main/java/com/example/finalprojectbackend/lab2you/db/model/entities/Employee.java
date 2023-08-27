package com.example.finalprojectbackend.lab2you.db.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.time.LocalDateTime;
@Entity
@Table(name="employees")
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cui;
    private String name;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String gender;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn (name="department_id")
    private Department department;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
    }
}

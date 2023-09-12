package com.example.finalprojectbackend.lab2you.db.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.time.LocalDateTime;
import java.util.List;

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
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String gender;
    @ManyToOne
    @JoinColumn (name="department_id")
    private Department department;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "assignedByEmployee")
    private List<Assignment> assignmentsGiven;

    @OneToMany(mappedBy = "assignedToEmployee")
    private List<Assignment> assignmentsReceived;

}

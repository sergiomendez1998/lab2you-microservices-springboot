package com.example.finalprojectbackend.lab2you.db.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name="employees")
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cui;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String gender;

    private Long createdBy;
    private Long updatedBy;
    @ManyToOne
    @JoinColumn (name="department_id")
    private DepartmentEntity departmentEntity;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "assignedByEmployee")
    private List<AssignmentEntity> assignmentsGiven;

    @OneToMany(mappedBy = "assignedToEmployee")
    private List<AssignmentEntity> assignmentsReceived;

}

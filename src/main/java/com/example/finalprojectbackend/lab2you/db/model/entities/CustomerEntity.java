package com.example.finalprojectbackend.lab2you.db.model.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name = "customers")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String expedientNumber;
    private String cui;
    private String firstName;
    private String lastName;
    private String nit;
    private String address;
    private String phoneNumber;
    private String gender;
    private String occupation;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    
    @OneToMany (mappedBy = "customer")
    private List<RequestEntity> requestEntities = new ArrayList<>();

}

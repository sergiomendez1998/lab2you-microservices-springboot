package com.example.finalprojectbackend.lab2you.db.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Table(name = "users")
public class UserEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickName;
    private String password;
    private String email;
    private  boolean enabled;
    private boolean resetPassword;
    private boolean confirmed;
    private String resetPasswordToken;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(mappedBy = "user")
    private Customer customer;

    @OneToOne(mappedBy = "user")
    private Employee employee;

}

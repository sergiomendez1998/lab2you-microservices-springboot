package com.example.finalprojectbackend.lab2you.db.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "users")
public class UserEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickName;
    private String password;
    private String email;
    private  boolean enabled;
    private boolean confirmed;
    private String userType;
    private String resetPasswordToken;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @OneToOne(mappedBy = "user")
    private CustomerEntity customer;

    @OneToOne(mappedBy = "user")
    private EmployeeEntity employee;

}

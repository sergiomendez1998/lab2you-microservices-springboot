package com.example.finalprojectbackend.lab2you.db.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "authorities")
public class AuthorityEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String path;
    private String icon;
    @ManyToMany(mappedBy ="authorities" )
    private List<RoleEntity> roleEntities;
    @OneToMany(mappedBy = "authorityEntity", fetch = FetchType.EAGER)
    private List<ModuleAuthority> moduleAuthorities = new ArrayList<>();
}

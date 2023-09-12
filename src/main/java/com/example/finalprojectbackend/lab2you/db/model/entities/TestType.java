package com.example.finalprojectbackend.lab2you.db.model.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Table(name = "test_types")
@NoArgsConstructor
public class TestType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @OneToMany (mappedBy = "testType")
    private List<Request> requests = new ArrayList<>();

    public TestType(String name, String description) {
        this.name = name;
        this.description = description;
    }
 }

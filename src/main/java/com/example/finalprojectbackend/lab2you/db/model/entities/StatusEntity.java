package com.example.finalprojectbackend.lab2you.db.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@Entity
@Table(name = "statuses")
public class StatusEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "request_status",
            joinColumns = @JoinColumn(name = "status_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "request_id", referencedColumnName = "id")
    )
    private List<RequestEntity> requests = new ArrayList<>();

    public StatusEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

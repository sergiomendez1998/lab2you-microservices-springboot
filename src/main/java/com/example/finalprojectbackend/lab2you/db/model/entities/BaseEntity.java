package com.example.finalprojectbackend.lab2you.db.model.entities;

import com.example.finalprojectbackend.lab2you.providers.CurrentUserProvider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity implements Serializable {

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private UserEntity updatedBy;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    private transient CurrentUserProvider currentUserProvider;

    @PrePersist
    public void prePersist() {
        UserEntity userEntity = currentUserProvider.getCurrentUser();
        if (userEntity != null) {
            createdBy = userEntity;
        }
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        UserEntity userEntity = currentUserProvider.getCurrentUser();
        if (userEntity != null) {
            updatedBy = userEntity;
        }
        updatedAt = LocalDateTime.now();
    }
}

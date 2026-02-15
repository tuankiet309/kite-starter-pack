package com.kite.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Base entity with UUID ID
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseUuidEntity extends BaseEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}

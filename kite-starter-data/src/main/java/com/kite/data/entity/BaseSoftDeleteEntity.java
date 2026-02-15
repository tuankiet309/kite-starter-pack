package com.kite.data.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDelete;

/**
 * Base entity with Soft Delete support (Hibernate 6.4+)
 */
@Getter
@Setter
@MappedSuperclass
@SoftDelete
public abstract class BaseSoftDeleteEntity extends BaseEntity {
}

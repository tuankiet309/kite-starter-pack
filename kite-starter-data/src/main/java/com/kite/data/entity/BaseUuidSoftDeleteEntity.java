package com.kite.data.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

/**
 * Convenience base class combining UUID ID and Soft Delete.
 */
@Getter
@Setter
@MappedSuperclass
@SoftDelete
public abstract class BaseUuidSoftDeleteEntity extends BaseUuidEntity {
}

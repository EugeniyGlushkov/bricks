package ru.briks.entity;


import jakarta.persistence.MappedSuperclass;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

@MappedSuperclass
public abstract class AbstractEntity<ID> {
    public abstract ID getId();
}

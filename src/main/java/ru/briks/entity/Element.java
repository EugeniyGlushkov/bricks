package ru.briks.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain=true)
@Entity
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Table(name = "elements")
public class Element extends DomainObject {

    @Column(name = "element_id")
    @NonNull
    @ToString.Include
    @EqualsAndHashCode.Include
    private String elementId;

    @Column(name = "part_id")
    @NonNull
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long partId;

    @Column(name = "color_id")
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long color_id;
}

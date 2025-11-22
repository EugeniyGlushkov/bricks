package ru.briks.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain=true)
@Entity
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Table(name = "themes")
public class Theme extends DomainObject {

    @ToString.Include
    @EqualsAndHashCode.Include
    private String name;

    @Column(name = "parent_id")
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long parentId;

}

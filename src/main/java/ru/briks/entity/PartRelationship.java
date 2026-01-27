package ru.briks.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

@Getter
@Setter
@Accessors(chain=true)
@Entity
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Table(name = "part_relationships")
public class PartRelationship extends DomainObject {

    @Column(name = "rel_type")
    @ToString.Include
    @EqualsAndHashCode.Include
    private PartRelationType relationType;

    @Column(name = "child_part_id")
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long childPartId;

    @Column(name = "parent_part_id")
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long parentPartId;
}

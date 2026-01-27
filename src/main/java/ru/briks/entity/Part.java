package ru.briks.entity;

import jakarta.persistence.*;
import lombok.*;
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
@Table(name = "parts")
public class Part extends DomainObject {

    @NonNull
    @Column(name = "part_num")
    @ToString.Include
    @EqualsAndHashCode.Include
    private String partNum;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Include
    @EqualsAndHashCode.Include
    private PartCategory partCategory;

    @Column(name = "part_material")
    @ToString.Include
    @EqualsAndHashCode.Include
    private String partMaterial;
}

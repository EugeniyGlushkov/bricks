package ru.briks.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "elements")
public class Element extends DomainObject {

    @Column(name = "element_id", unique = true, length = 10)
    @NotNull(message = "Element's id is required")
    @ToString.Include
    private String elementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Part is required")
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id")
    private Color color;
}

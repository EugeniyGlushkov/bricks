package ru.briks.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

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

    @NotNull(message = "Part's num is required")
    @Column(name = "part_num", nullable = false, unique = true, length = 20)
    @ToString.Include
    private String partNum;

    @ToString.Include
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_cat_id")
    private PartCategory partCategory;

    @Column(name = "part_material")
    private String partMaterial;

    @Column(name = "part_num_bricklink", length = 20)
    private String partNumBricklink;

    @Column(name = "category_name_bricklink", length = 255)
    private String categoryNameBricklink;

    @Column(name = "name_bricklink")
    private String nameBricklink;

    @Column(name = "weight_grams", precision = 7, scale = 2)
    private BigDecimal weightGrams;
}

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
@Table(name = "inventory_sets")
public class InventorySet extends DomainObject {

    @Column(name = "inventory_id")
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long inventoryId;

    @Column(name = "set_num")
    @ToString.Include
    @EqualsAndHashCode.Include
    private String setNum;

    @ToString.Include
    @EqualsAndHashCode.Include
    private Integer quantity;
}

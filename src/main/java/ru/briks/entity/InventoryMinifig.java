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
@Table(name = "inventory_minifigs")
public class InventoryMinifig extends DomainObject {

    @Column(name = "inventory_id")
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long inventoryId;

    @Column(name = "fig_num")
    @ToString.Include
    @EqualsAndHashCode.Include
    private String figNum;

    @ToString.Include
    @EqualsAndHashCode.Include
    private Integer quantity;
}

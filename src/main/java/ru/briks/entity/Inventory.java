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
@Table(name = "inventories")
public class Inventory extends DomainObject {

    @ToString.Include
    @EqualsAndHashCode.Include
    private Integer version;

    @Column(name = "set_num")
    @ToString.Include
    @EqualsAndHashCode.Include
    private String setNum;
}

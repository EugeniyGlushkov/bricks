package ru.briks.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author EGlushkov
 * Date: 29.04.2026
 * Time: 18:55
 */

@Entity
@Table(name = "element_external_ids",
        uniqueConstraints = @UniqueConstraint(columnNames = "element_external_id"))
@Getter
@Setter
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class ElementExternalId extends DomainObject {

    @Column(name = "element_external_id", nullable = false, length = 20)
    @ToString.Include
    private String externalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_id", nullable = false, foreignKey = @ForeignKey(name = "ext_ids_element_fk"))
    private Element element;
}
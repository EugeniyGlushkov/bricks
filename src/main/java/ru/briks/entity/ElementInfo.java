package ru.briks.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CurrentTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author EGlushkov
 * Date: 29.03.2026
 * Time: 20:35
 */

@Getter
@Setter
@Builder
@Accessors(chain=true)
@Entity
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Table(name = "element_info")
public class ElementInfo extends DomainObject {
    @Column(name = "element_id")
    @NonNull
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long elementId;

    @Column(name = "state")
    @ToString.Include
    @EqualsAndHashCode.Include
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "count")
    @ToString.Include
    @EqualsAndHashCode.Include
    private Integer count;

    @Column(name = "price")
    @ToString.Include
    @EqualsAndHashCode.Include
    private BigDecimal price;

    @Column(name = "price_kuboka")
    @ToString.Include
    @EqualsAndHashCode.Include
    private BigDecimal priceKuboka;

    @Column(name = "created")
    @CurrentTimestamp
    private LocalDateTime created;

    @Column(name = "updated")
    private LocalDateTime updated;

    @Column(name = "count_updated")
    private LocalDateTime countUpdated;

    @Column(name = "price_updated")
    private LocalDateTime priceUpdated;

    @Column(name = "price_kuboka_updated")
    private LocalDateTime priceKubokaUpdated;

    @PrePersist
    @PreUpdate
    public void updated() {
        updated = LocalDateTime.now();
    }
}

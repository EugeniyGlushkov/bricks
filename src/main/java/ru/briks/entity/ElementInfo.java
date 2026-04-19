package ru.briks.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Table(name = "element_info")
public class ElementInfo extends DomainObject {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_id", nullable = false)
    @NotNull(message = "Element is required")
    @ToString.Include // Чтобы в логах видеть ID элемента
    @EqualsAndHashCode.Include
    private Element element;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 20)
    @ToString.Include
    @EqualsAndHashCode.Include
    @NotNull
    private State state;

    @Column(name = "count")
    @Min(value = 0, message = "Количество не может быть отрицательным")
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long count;

    @Column(name = "price", precision = 10, scale = 2)
    @DecimalMin(value = "0.01", message = "Цена должна быть > 0")
    @ToString.Include
    @EqualsAndHashCode.Include
    private BigDecimal price;

    @Column(name = "price_kuboka", precision = 10, scale = 2)
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

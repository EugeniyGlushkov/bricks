package ru.briks.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

@Getter
@Setter
@Builder
@Accessors(chain=true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "elements")
public class Element extends DomainObject {

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Part is required")
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id")
    private Color color;

    @OneToMany(mappedBy = "element", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ElementExternalId> externalIds = new ArrayList<>();

    public void addExternalId(String externalId) {
        var idEntity = ElementExternalId.builder()
                .externalId(externalId)
                .element(this)
                .build();
        externalIds.add(idEntity);
    }

    public void removeExternalId(String externalId) {
        externalIds.removeIf(e -> e.getExternalId().equals(externalId));
    }
}
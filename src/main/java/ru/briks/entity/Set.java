package ru.briks.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
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
@Table(name = "sets")
public class Set extends DomainObject {

    @Column(name = "set_num")
    @NonNull
    @ToString.Include
    @EqualsAndHashCode.Include
    private String setNum;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String name;

    @ToString.Include
    @EqualsAndHashCode.Include
    private Integer year;

    @Column(name = "theme_id")
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long themeId;

    @Column(name = "num_parts")
    @ToString.Include
    @EqualsAndHashCode.Include
    private Integer numParts;

    @Column(name = "outer_img_url")
    @ToString.Include
    @EqualsAndHashCode.Include
    private String outerImgUrl;

    @Column(name = "img_path")
    @ToString.Include
    @EqualsAndHashCode.Include
    private String imgPath;
}

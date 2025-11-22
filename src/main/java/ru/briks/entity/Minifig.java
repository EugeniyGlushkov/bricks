package ru.briks.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain=true)
@Entity
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Table(name = "minifigs")
public class Minifig extends DomainObject {

    @NonNull
    @Column(name = "fig_num")
    @ToString.Include
    @EqualsAndHashCode.Include
    private String figNum;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String name;

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

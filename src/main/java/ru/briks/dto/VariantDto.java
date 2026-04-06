package ru.briks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author EGlushkov
 * Date: 30.03.2026
 * Time: 19:15
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class VariantDto {
    private String variantOf;
    private String variantType;
}

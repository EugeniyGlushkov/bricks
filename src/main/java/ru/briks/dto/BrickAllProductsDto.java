package ru.briks.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * @author EGlushkov
 * Date: 30.03.2026
 * Time: 0:48
 */

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrickAllProductsDto {
    @JsonProperty("products")
    private List<BrickProductDto> products;
}

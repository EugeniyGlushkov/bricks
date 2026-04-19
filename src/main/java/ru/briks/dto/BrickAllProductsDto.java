package ru.briks.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

package ru.briks.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author EGlushkov
 * Date: 29.03.2026
 * Time: 23:02
 */

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrickProductDto {
    @JsonProperty("model")
    private String model;
    @JsonProperty("price")
    private String price;
    @JsonProperty("manufacturer")
    private String manufacturer;
}

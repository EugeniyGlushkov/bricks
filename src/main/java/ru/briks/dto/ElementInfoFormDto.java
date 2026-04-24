package ru.briks.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.briks.entity.State;

import java.math.BigDecimal;

/**
 * @author EGlushkov
 * Date: 23.04.2026
 * Time: 18:34
 */

@Data
public class ElementInfoFormDto {
    private Long id;  // null для новой записи

    @NotNull(message = "Элемент обязателен")
    private Long elementId;

    // Для редиректа после сохранения (не валидируется)
    private Long partId;

    @NotNull(message = "Состояние обязательно")
    private State state;

    @NotNull(message = "Остаток обязателен")
    @DecimalMin(value = "0", message = "Остаток не может быть отрицательным")
    private Long count;

    @DecimalMin(value = "0.01", inclusive = false, message = "Цена должна быть больше 0")
    private BigDecimal price;

    @DecimalMin(value = "0.01", inclusive = false, message = "Цена (Кубока) должна быть больше 0")
    private BigDecimal priceKuboka;
}

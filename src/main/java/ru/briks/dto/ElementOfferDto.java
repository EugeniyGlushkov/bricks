package ru.briks.dto;

import ru.briks.entity.State;

import java.math.BigDecimal;

public record ElementOfferDto(
        // Element + Part + Color
        Long elementId,
        String partNum,
        String partName,
        String colorName,
        String rgb,

        // ElementInfo (nullable)
        Long infoId,
        State state,
        Long count,
        BigDecimal price,
        BigDecimal priceKuboka,

        // InventoryPart image
        String imageUrl
) {
}

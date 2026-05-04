package ru.briks.dto;

import ru.briks.entity.State;

import java.math.BigDecimal;
import java.util.List;

public record ElementOfferDto(
        // Element + Part + Color
        Long elementId,
        Long partId,
        Long colorId,
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
        ,
        // ElementExternalId externalId
        List<String> externalIds
) {
}
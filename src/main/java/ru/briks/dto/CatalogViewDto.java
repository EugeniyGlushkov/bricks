package ru.briks.dto;

import ru.briks.entity.State;

import java.math.BigDecimal;

public record CatalogViewDto(
        Long elementId,
        String partNum,
        String partName,
        String colorName,
        String rgb,
        Long infoId,
        State state,
        Long count,
        BigDecimal price,
        String imageUrl
) {
    public static CatalogViewDto from(ElementOfferDto src) {
        return new CatalogViewDto(
                src.elementId(), src.partNum(), src.partName(), src.colorName(), src.rgb(),
                src.infoId(), src.state(), src.count(), src.price(), src.imageUrl()
        );
    }
}

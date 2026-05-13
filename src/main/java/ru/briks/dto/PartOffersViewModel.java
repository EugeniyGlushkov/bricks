package ru.briks.dto;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author EGlushkov
 * Date: 12.05.2026
 * Time: 13:31
 */

public record PartOffersViewModel(
        String partNum,
        String partNumBricklink,
        Page<ElementOfferDto> offers,
        List<PartAdminDto> analogParts
) {
    public static PartOffersViewModel of(String partNum,
                                         String partNumBricklink,
                                         Page<ElementOfferDto> offers,
                                         List<PartAdminDto> analogParts) {
        return new PartOffersViewModel(partNum, partNumBricklink, offers, analogParts);
    }
}
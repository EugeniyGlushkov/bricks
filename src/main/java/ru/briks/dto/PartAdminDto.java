package ru.briks.dto;

public record PartAdminDto(
        Long id,
        String partNum,
        String partNumBricklink,
        String name,
        Long categoryId,
        String imageUrl
) {
}

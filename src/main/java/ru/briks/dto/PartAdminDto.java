package ru.briks.dto;

public record PartAdminDto(
        Long id,
        String partNum,
        String name,
        Long categoryId,
        String imageUrl
) {
}

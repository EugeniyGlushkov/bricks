package ru.briks.dto.report;

public record ReportBlockDto(
        String description,
        String partNum,
        String price,
        String colorName,
        byte[] imageBytes
) {
}
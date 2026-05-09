package ru.briks.service.report;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.briks.dto.ElementOfferDto;
import ru.briks.dto.report.ReportBlockDto;
import ru.briks.entity.State;
import ru.briks.service.ElementService;
import ru.briks.utils.ImageUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author EGlushkov
 * Date: 06.05.2026
 * Time: 14:26
 */

@Service
@RequiredArgsConstructor
public class ExcelReportService {

    private final ElementService elementService;

    private static final int BLOCKS_PER_ROW = 12;
    private static final int BLOCKS_PER_PAGE = 72;
    private static final int ROWS_PER_BLOCK = 10;
    private static final int MAX_IMG_SIZE = 130;
    private static final Pattern DIM_3_PATTERN = Pattern.compile("(?i)\\d+(?:\\.\\d+)?\\s*x\\s*\\d+(?:\\.\\d+)?\\s*x\\s*\\d+(?:\\.\\d+)?");
    private static final Pattern DIM_2_PATTERN = Pattern.compile("(?i)\\d+(?:\\.\\d+)?\\s*x\\s*\\d+(?:\\.\\d+)?");


    public byte[] generatePartsReport(
            List<Long> categoryIds,
            Set<State> states,
            boolean onlyWithPrice,
            boolean onlyInStock
    ) {
        List<ElementOfferDto> rawData =
                elementService.getOffersForCategory(categoryIds, states, onlyWithPrice, onlyInStock);
        if (rawData.isEmpty()) {
            return generateEmptyReport();
        }

        // 1. Агрегация в ReportBlockDto
        List<ReportBlockDto> blocks = aggregateBlocks(rawData);

        // 2. Генерация Excel
        try (InputStream is = new ClassPathResource("reports/templates/parts_report_template.xlsx").getInputStream();
             Workbook wb = new XSSFWorkbook(is)) {

            Sheet sheet = wb.getSheetAt(0);
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            CreationHelper helper = wb.getCreationHelper();

            CellStyle styleHeader = getCellStyleSafe(sheet, 0, 0);
            CellStyle styleId = getCellStyleSafe(sheet, 8, 0);
            CellStyle stylePrice = getCellStyleSafe(sheet, 8, 1);
            CellStyle styleColor = getCellStyleSafe(sheet, 9, 0);

            for (int i = 0; i < blocks.size(); i++) {
                ReportBlockDto block = blocks.get(i);
                int colOffset = (i % BLOCKS_PER_ROW) * 2;
                int baseRow = (i / BLOCKS_PER_ROW) * ROWS_PER_BLOCK;

                // Копируем высоту строк из шаблона
                for (int r = 0; r < ROWS_PER_BLOCK; r++) {
                    Row row = sheet.getRow(baseRow + r);
                    if (row == null) {
                        row = sheet.createRow(baseRow + r);
                    }
                    Row tpl = sheet.getRow(r);
                    if (tpl != null) {
                        row.setHeight(tpl.getHeight());
                    }
                }

                // 🔹 Строка 1: Описание (объединено)
                Cell cellA1 = getOrCreateCell(sheet, baseRow, colOffset);
                Cell cellB1 = getOrCreateCell(sheet, baseRow, colOffset + 1);
                cellA1.setCellStyle(styleHeader);
                cellB1.setCellStyle(styleHeader); // ← важно: применяем к обеим
                cellA1.setCellValue(block.description());

                CellRangeAddress headerArea = new CellRangeAddress(baseRow, baseRow, colOffset, colOffset + 1);
                sheet.addMergedRegion(headerArea);

                RegionUtil.setBorderTop(BorderStyle.THIN, headerArea, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, headerArea, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, headerArea, sheet);

                // 🔹 Строки 2-8: Картинка
                if (block.imageBytes() != null) {
                    CellRangeAddress imgArea = new CellRangeAddress(baseRow + 1, baseRow + 7, colOffset, colOffset + 1);
                    RegionUtil.setBorderLeft(BorderStyle.THIN, imgArea, sheet);
                    RegionUtil.setBorderRight(BorderStyle.THIN, imgArea, sheet);

                    int picIdx = wb.addPicture(block.imageBytes(), Workbook.PICTURE_TYPE_JPEG);
                    ClientAnchor anchor = helper.createClientAnchor();
                    anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);

                    anchor.setCol1(colOffset);
                    anchor.setCol2(colOffset + 2);
                    anchor.setRow1(baseRow + 1);
                    anchor.setRow2(baseRow + 8);

                    // ✅ Отступы внутри якоря (5px ≈ 47625 EMU)
                    int paddingEMU = 5 * 9525;
                    anchor.setDx1(paddingEMU);   // Лево
                    anchor.setDy1(paddingEMU);   // Верх
                    anchor.setDx2(-paddingEMU);  // Право
                    anchor.setDy2(-paddingEMU);  // Низ

                    drawing.createPicture(anchor, picIdx).resize(1.0);
                }

                // 🔹 Строка 9: ID и Цена
                Cell idCell = getOrCreateCell(sheet, baseRow + 8, colOffset);
                idCell.setCellStyle(styleId);
                idCell.setCellValue(block.partNum());

                Cell priceCell = getOrCreateCell(sheet, baseRow + 8, colOffset + 1);
                priceCell.setCellStyle(stylePrice);
                priceCell.setCellValue(block.price() + " р");

                // 🔹 Строка 10: Цвет (объединено)
                Cell cellA10 = getOrCreateCell(sheet, baseRow + 9, colOffset);
                Cell cellB10 = getOrCreateCell(sheet, baseRow + 9, colOffset + 1);
                cellA10.setCellStyle(styleColor);
                cellB10.setCellStyle(styleColor);
                cellA10.setCellValue(block.colorName());

                CellRangeAddress colorArea = new CellRangeAddress(baseRow + 9, baseRow + 9, colOffset, colOffset + 1);
                sheet.addMergedRegion(colorArea);

                RegionUtil.setBorderBottom(BorderStyle.THIN, colorArea, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, colorArea, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, colorArea, sheet);

                // 📄 Разрыв страницы
                if ((i + 1) % BLOCKS_PER_PAGE == 0 && i < blocks.size() - 1) {
                    sheet.setRowBreak(baseRow + ROWS_PER_BLOCK - 1);
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка генерации Excel-отчёта", e);
        }
    }

    // === Вспомогательные методы ===

    private List<ReportBlockDto> aggregateBlocks(List<ElementOfferDto> rawData) {
        // Группируем по partId (LinkedHashMap сохраняет порядок partNum)
        Map<Long, List<ElementOfferDto>> byPart = rawData.stream()
                .collect(java.util.stream.Collectors.groupingBy(ElementOfferDto::partId, LinkedHashMap::new, java.util.stream.Collectors.toList()));

        List<ReportBlockDto> result = new ArrayList<>();

        for (List<ElementOfferDto> partGroup : byPart.values()) {
            String partNum = partGroup.get(0).partNum();
            String description = extractDimensions(partGroup.get(0).partName());

            // Группируем внутри детали по цене (TreeMap сортирует по возрастанию)
            Map<BigDecimal, List<ElementOfferDto>> byPrice = partGroup.stream()
                    .filter(dto -> dto.price() != null)
                    .collect(java.util.stream.Collectors.groupingBy(ElementOfferDto::price, TreeMap::new, java.util.stream.Collectors.toList()));

            for (Map.Entry<BigDecimal, List<ElementOfferDto>> priceEntry : byPrice.entrySet()) {
                List<ElementOfferDto> priceGroup = priceEntry.getValue();
                String priceStr = priceEntry.getKey().toPlainString();

                // Уникальные цвета
                Set<String> colors = priceGroup.stream()
                        .map(ElementOfferDto::colorName)
                        .filter(Objects::nonNull)
                        .collect(java.util.stream.Collectors.toSet());
                String colorLabel = colors.size() == 1 ? colors.iterator().next() : colors.size() + " colors";

                // Фолбэк картинки: пробуем скачать первую доступную
                byte[] img = null;
                for (ElementOfferDto dto : priceGroup) {
                    if (dto.imageUrl() != null) {
                        img = ImageUtils.downloadAndResize(dto.imageUrl(), MAX_IMG_SIZE);
                        if (img != null) break;
                    }
                }
                result.add(new ReportBlockDto(description, partNum, priceStr, colorLabel, img));
            }
        }
        return result;
    }

    private String extractDimensions(String name) {
        if (name == null) {
            return "";
        }

        // 1. Ищем приоритетный формат: A x B x C
        Matcher m3 = DIM_3_PATTERN.matcher(name);
        if (m3.find()) {
            return m3.group().trim();
        }

        // 2. Если нет, ищем формат: A x B
        Matcher m2 = DIM_2_PATTERN.matcher(name);
        if (m2.find()) {
            return m2.group().trim();
        }

        // 3. Не найдено → пустая строка
        return "";
    }

    private CellStyle getCellStyleSafe(Sheet sheet, int row, int col) {
        Row r = sheet.getRow(row);
        Cell c = r != null ? r.getCell(col) : null;
        if (c == null)
            throw new IllegalStateException("Шаблон повреждён: ячейка " + (col + 1) + ":" + (row + 1) + " не найдена");
        return c.getCellStyle();
    }

    private Cell getOrCreateCell(Sheet sheet, int r, int c) {
        Row row = sheet.getRow(r);
        if (row == null) row = sheet.createRow(r);
        Cell cell = row.getCell(c);
        return cell != null ? cell : row.createCell(c);
    }

    private byte[] generateEmptyReport() {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Отчёт");
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("Нет данных по выбранным фильтрам");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка генерации пустого отчёта", e);
        }
    }
}

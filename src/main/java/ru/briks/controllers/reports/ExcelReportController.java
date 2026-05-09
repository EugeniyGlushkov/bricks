package ru.briks.controllers.reports;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.briks.entity.State;
import ru.briks.service.report.ExcelReportService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * @author EGlushkov
 * Date: 06.05.2026
 * Time: 16:46
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ExcelReportController {

    private final ExcelReportService reportService;

    @GetMapping("/parts/category")
    public void downloadReport(
            @RequestParam List<Long> categoryIds,
            @RequestParam(required = false) Set<State> states,
            @RequestParam(required = false, defaultValue = "false") boolean onlyWithPrice,
            @RequestParam(required = false, defaultValue = "false") boolean onlyInStock,
            HttpServletResponse response) {

        byte[] excelData = reportService.generatePartsReport(categoryIds, states, onlyWithPrice, onlyInStock);

        String fileName = String.format("parts_report_%s.xlsx", LocalDate.now());
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
        response.setContentLength(excelData.length);

        try {
            response.getOutputStream().write(excelData);
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка записи в HTTP ответ", e);
        }
    }
}

package ru.briks.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.thymeleaf.exceptions.TemplateInputException;

/**
 * @author EGlushkov
 * Date: 17.04.2026
 * Time: 14:13
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    // Обработка ошибок парсинга Thymeleaf (критично сейчас!)
    @ExceptionHandler(TemplateInputException.class)
    public String handleTemplateError(TemplateInputException ex, Model model) {
        model.addAttribute("errorMessage", "Ошибка шаблона: " + ex.getMessage());
        // Возвращаем простую страницу ошибки, чтобы видеть проблему, а не 404/500
        return "error";
    }

    // Обработка 404 (страница не найдена)
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        model.addAttribute("errorMessage", "Страница не найдена: " + ex.getRequestURL());
        return "error";
    }

    // Глобальный обработчик для всего остального
    @ExceptionHandler(Exception.class)
    public String handleGenericError(Exception ex, Model model) {
        model.addAttribute("errorMessage", "Внутренняя ошибка: " + ex.getClass().getSimpleName());
        // В логах стектрейс уже будет, тут показываем пользователю только суть
        return "error";
    }
}

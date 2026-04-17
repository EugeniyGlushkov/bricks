package ru.briks.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author EGlushkov
 * Date: 17.04.2026
 * Time: 13:14
 */

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "index";
    }
}

package ru.briks.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author EGlushkov
 * Date: 17.04.2026
 * Time: 15:28
 */

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}

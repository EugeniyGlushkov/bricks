package ru.briks.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author EGlushkov
 * Date: 17.04.2026
 * Time: 13:21
 */

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping
    public String dashboard() {
        return "admin/index";
    }
}

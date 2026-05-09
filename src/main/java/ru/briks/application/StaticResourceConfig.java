package ru.briks.application;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author EGlushkov
 * Date: 09.05.2026
 * Time: 14:16
 */

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**", "/css/**", "/images/**", "/favicon.ico")
                .addResourceLocations("classpath:/static/js/",
                        "classpath:/static/css/",
                        "classpath:/static/images/",
                        "classpath:/static/");
    }
}
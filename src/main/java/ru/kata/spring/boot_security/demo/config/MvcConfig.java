package ru.kata.spring.boot_security.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");

        registry.addViewController("/admin").setViewName("admin");

        registry.addViewController("/auth/login").setViewName("auth/login");
        registry.addViewController("/auth/register").setViewName("auth/registration");

        registry.addViewController("/users").setViewName("users/index");
        registry.addViewController("/users/{id}/edit").setViewName("user-edit");
        registry.addViewController("/users/{id}").setViewName("users/show");
    }
}

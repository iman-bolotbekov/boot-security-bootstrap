package ru.kata.spring.boot_security.demo.controllers;

import org.hibernate.Hibernate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.security.CustomUserDetails;
import ru.kata.spring.boot_security.demo.services.CustomUserDetailsService;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class UserController {
    private final CustomUserDetailsService service;
    public UserController(CustomUserDetailsService service) {
        this.service = service;
    }
    @GetMapping
    public ModelAndView index(ModelAndView modelAndView) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = service.findOne(customUserDetails.getUser().getId());
        Hibernate.initialize(user.getRoles());
        modelAndView.addObject("user", user);
        modelAndView.setViewName("users/index");
        return modelAndView;
    }
}

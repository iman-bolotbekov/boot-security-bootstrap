package ru.kata.spring.boot_security.demo.controllers;

import org.hibernate.Hibernate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.security.CustomUserDetails;
import ru.kata.spring.boot_security.demo.services.CustomUserDetailsService;

@Controller
@RequestMapping("/")
public class IndexController {
    private final CustomUserDetailsService customUserDetailsService;
    public IndexController(final CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }
    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetailsService.findOne(customUserDetails.getUser().getId());
        Hibernate.initialize(user.getRoles());
        modelAndView.addObject("user", user);
        modelAndView.setViewName("index");
        return modelAndView;
    }
}

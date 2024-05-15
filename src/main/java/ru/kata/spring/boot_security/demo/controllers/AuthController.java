package ru.kata.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RegistrationService;
import ru.kata.spring.boot_security.demo.utils.UserValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserValidator userValidator;
    private final RegistrationService registerService;
    public AuthController(UserValidator userValidator, RegistrationService registerService) {
        this.userValidator = userValidator;
        this.registerService = registerService;
    }
    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("auth/login");
    }
    @GetMapping("/register")
    public ModelAndView registrationPage(@ModelAttribute("user") User user) {
        return new ModelAndView("auth/registration");
    }
    @PostMapping("/register")
    public ModelAndView performRegistration(@ModelAttribute("user") @Valid User user,
                                            BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("admin");
        this.userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("auth/registration");
            return modelAndView;
        }
        this.registerService.register(user);
        modelAndView.setViewName("redirect:/auth/login");
        return modelAndView;
    }
}

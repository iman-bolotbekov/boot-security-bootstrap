package ru.kata.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.hibernate.Hibernate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.security.CustomUserDetails;
import org.springframework.stereotype.Controller;
import ru.kata.spring.boot_security.demo.services.CustomUserDetailsService;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.utils.UserValidator;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserValidator userValidator;
    private final RoleService roleService;
    private final CustomUserDetailsService userService;
    private final CustomUserDetailsService customUserDetailsService;
    public AdminController(CustomUserDetailsService customUserDetailsService,
                           RoleService roleService,
                           CustomUserDetailsService userService,
                           UserValidator userValidator) {
        this.customUserDetailsService = customUserDetailsService;
        this.roleService = roleService;
        this.userService = userService;
        this.userValidator = userValidator;
    }
    @GetMapping
    public ModelAndView adminPage() {
        ModelAndView modelAndView = new ModelAndView("admin");
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = customUserDetailsService.findOne(customUserDetails.getUser().getId());
        Hibernate.initialize(user.getRoles());
        modelAndView.addObject("user", user);
        modelAndView.addObject("users", customUserDetailsService.findAll());
        modelAndView.addObject("userForCreate", new User());
        modelAndView.addObject("rolesForCreate", roleService.findAll());
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @GetMapping("/users/{id}")
    public ModelAndView show(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("users/show");
        modelAndView.addObject("user", userService.findOne(id));
        return modelAndView;
    }
    @PostMapping("/users")
    public ModelAndView create(@ModelAttribute("userForCreate") @Valid User user,
                               @RequestParam(value = "selectedRoles", required = false) List<Integer> selectedRoles,
                               BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("create");
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("admin");
        }
        userService.save(user, selectedRoles);
        modelAndView.setViewName("redirect:/admin");
        return modelAndView;
    }
    @GetMapping("/users/{id}/edit")
    public ModelAndView edit(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("edit");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.findOne(customUserDetails.getUser().getId());

        modelAndView.addObject("user", user);
        modelAndView.addObject("users", userService.findAll());
        modelAndView.addObject("userForCreate", new User());
        modelAndView.addObject("rolesForCreate", roleService.findAll());
        modelAndView.addObject("userForEdit", userService.findOne(id));
        modelAndView.setViewName("admin");
        return modelAndView;
    }
    @PatchMapping("/users/{id}")
    public ModelAndView update(@PathVariable("id") int id,
                               @ModelAttribute("user") @Valid User user,
                               BindingResult bindingResult,
                               @RequestParam(value = "selectedRoles", required = false) List<Integer> selectedRoles) {
        ModelAndView modelAndView = new ModelAndView("user-update");
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("users/edit");
        } else {
            userService.update(id, user, selectedRoles);
            modelAndView.setViewName("redirect:/admin");
        }
        return modelAndView;
    }
    @GetMapping("/users/{id}/delete")
    public ModelAndView preDelete(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("pre-delete");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.findOne(customUserDetails.getUser().getId());
        modelAndView.addObject("user", user);
        modelAndView.addObject("users", userService.findAll());
        modelAndView.addObject("userForCreate", new User());
        modelAndView.addObject("rolesForCreate", roleService.findAll());
        modelAndView.addObject("userForDelete", userService.findOne(id));
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @DeleteMapping("/users/{id}")
    public ModelAndView delete(@PathVariable("id") int id) {
        userService.delete(id);
        return new ModelAndView("redirect:/admin");
    }
}

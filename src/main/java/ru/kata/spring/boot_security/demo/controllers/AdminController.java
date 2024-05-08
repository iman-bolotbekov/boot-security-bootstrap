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

    public AdminController(CustomUserDetailsService customUserDetailsService,
                           RoleService roleService,
                           CustomUserDetailsService userService,
                           UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping
    public ModelAndView adminPage() {
        ModelAndView modelAndView = getModelAndView("admin");
        return modelAndView;
    }

    @GetMapping("/users/{id}")
    public ModelAndView showUser(@PathVariable("id") int id) {
        ModelAndView modelAndView = getModelAndView("users/show");
        modelAndView.addObject("user", userService.findOne(id));
        return modelAndView;
    }

    @PostMapping("/users")
    public ModelAndView createUser(@ModelAttribute("userForCreate") @Valid User user,
                                   @RequestParam(value = "selectedRoles", required = false) List<Integer> selectedRoles,
                                   BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("create");
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("admin");
        } else {
            userService.save(user, selectedRoles);
            modelAndView.setViewName("redirect:/admin");
        }
        return modelAndView;
    }

    @GetMapping("/users/{id}/edit")
    public ModelAndView editUser(@PathVariable("id") int id) {
        ModelAndView modelAndView = getModelAndView("edit");
        modelAndView.addObject("userForEdit", userService.findOne(id));
        return modelAndView;
    }

    @PatchMapping("/users/{id}")
    public ModelAndView updateUser(@PathVariable("id") int id,
                                   @ModelAttribute("userForEdit") @Valid User user,
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
    public ModelAndView preDeleteUser(@PathVariable("id") int id) {
        ModelAndView modelAndView = getModelAndView("pre-delete");
        modelAndView.addObject("userForDelete", userService.findOne(id));
        return modelAndView;
    }

    @DeleteMapping("/users/{id}")
    public ModelAndView deleteUser(@PathVariable("id") int id) {
        userService.delete(id);
        return new ModelAndView("redirect:/admin");
    }

    private ModelAndView getModelAndView(String viewName) {
        ModelAndView modelAndView = new ModelAndView(viewName);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.findOne(customUserDetails.getUser().getId());
        modelAndView.addObject("user", user);
        modelAndView.addObject("users", userService.findAll());
        modelAndView.addObject("userForCreate", new User());
        modelAndView.addObject("rolesForCreate", roleService.findAll());
        return modelAndView;
    }
}

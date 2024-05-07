package ru.kata.spring.boot_security.demo.utils;

import ru.kata.spring.boot_security.demo.services.CustomUserDetailsService;
import ru.kata.spring.boot_security.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final CustomUserDetailsService customUserDetailsService;
    public UserValidator(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }
    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (customUserDetailsService.findByUsername(user.getUsername()).isPresent()) {
            errors.rejectValue("username", "400", "Пользователь с таким именем уже существует");
        }
    }
}

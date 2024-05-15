package ru.kata.spring.boot_security.demo.utils;

import ru.kata.spring.boot_security.demo.models.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.services.UserService;

@Component
public class UserValidator implements Validator {
    private final UserService userService;
    public UserValidator(UserService userService) {
        this.userService = userService;
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }
    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            errors.rejectValue("username", "400", "Пользователь с таким именем уже существует");
        }
    }
}

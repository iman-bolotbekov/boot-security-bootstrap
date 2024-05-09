package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

public interface RegistrationService {
    void register(User user);
}

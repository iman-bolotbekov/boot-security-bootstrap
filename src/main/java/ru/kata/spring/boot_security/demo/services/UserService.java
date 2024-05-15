package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public abstract class UserService implements UserDetailsService {
    public abstract Optional<User> findByUsername(String username);
    public abstract List<User> findAll();
    public abstract User findOne(int id);
    public abstract void save(User user, List<Integer> roleIds);
    public abstract void update(int id, User updatedPerson, List<Integer> roleIds);
    public abstract void delete(int id);
}

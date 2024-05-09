package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAll();
    Role findOne(int id);
    Role save(Role role);
    Optional<Role> findByName(String name);
    void update(int id, Role role);
    void delete(int id);
}

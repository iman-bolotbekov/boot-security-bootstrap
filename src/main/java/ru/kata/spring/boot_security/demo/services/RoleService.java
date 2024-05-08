package ru.kata.spring.boot_security.demo.services;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
    public Role findOne(int id) {
        return roleRepository.findById(id).orElse(null);
    }
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
    public Role save(Role role) {
        return roleRepository.save(role);
    }
    public void update(int id, Role updatedRole) {
        updatedRole.setId(id);
        roleRepository.save(updatedRole);
    }
    public void delete(int id) {
        roleRepository.deleteById(id);
    }
}

package ru.kata.spring.boot_security.demo.services;

import org.springframework.context.annotation.Lazy;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.security.UserDetailsImpl;

import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl extends UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    public UserDetailsServiceImpl(UserRepository userRepository,
                                  RoleRepository roleRepository,
                                  @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }
        Hibernate.initialize(user.get().getRoles());
        return new UserDetailsImpl(user.get());
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }
    public User findOne(int id) {
        Optional<User> foundPerson = userRepository.findById(id);
        return foundPerson.orElse(null);
    }
    @Transactional
    public void save(User user, List<Integer> roleIds) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAge(user.getAge());
        user.setEmail(user.getEmail());
        if (roleIds != null) {
            for (Integer roleId : roleIds) {
                roleRepository.findById(roleId).ifPresent(user::addRole);
            }
        } else {
            roleRepository.findByName("ROLE_USER").ifPresent(user::addRole);
        }
        userRepository.save(user);
    }
    @Transactional
    public void update(int id, User updatedPerson, List<Integer> roleIds) {
        updatedPerson.setId(id);
        updatedPerson.setPassword(userRepository.findById(id).get().getPassword());
        updatedPerson.setRoles(userRepository.findById(id).get().getRoles());
        if (roleIds != null) {
            Set<Role> roles = new HashSet<>();
            for (Integer roleId : roleIds) {
                roles.add(roleRepository.findById(roleId).get());
            }
            updatedPerson.setRoles(roles);
        }
        userRepository.save(updatedPerson);
    }
    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }

}

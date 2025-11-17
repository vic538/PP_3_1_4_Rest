package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public User findUserByName(String name) {
        return userRepository.findUserByName(name);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public String findEmailByName(String name) {
        return userRepository.findEmailByName(name);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User getUser(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public void saveUserWithRole(User user) {
        List<String> rolesStr = user.getRoles().stream()
                .map(Role::getAuthority)
                .collect(Collectors.toList());

        if (user.getId() == null || user.getId() == 0) {

            List<Role> roles = processRoles(rolesStr);
            user.setRoles(roles);
            user.setEnabled(true);

            saveUser(user);
        } else {

            User existingUser = userRepository.findById(user.getId()).orElse(null);

            if (rolesStr != null && !rolesStr.isEmpty()) {
                List<Role> updatedRoles = processRoles(rolesStr);
                user.setRoles(updatedRoles);
            } else {
                user.setRoles(existingUser.getRoles());
            }

            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            }
            user.setEnabled(existingUser.isEnabled());
            saveUser(user);
        }
    }

    private List<Role> processRoles(List<String> roleNames) {
        List<Role> roles = new ArrayList<>();
        if (roleNames != null && !roleNames.isEmpty()) {
            for (String roleName : roleNames) {
                Role role = roleService.findByAuthority(roleName);
                if (role == null) {
                    role = new Role(roleName);
                    roleService.save(role);
                }
                roles.add(role);
            }
        }
        return roles;
    }

    @Override
    public void updateUserWithRoles(int id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        List<String> rolesStr = user.getRoles().stream()
                .map(Role::getAuthority)
                .collect(Collectors.toList());

        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setAge(user.getAge());
        existingUser.setEmail(user.getEmail());
        existingUser.setEnabled(user.isEnabled());

        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        List<Role> roles = processRoles(rolesStr);
        existingUser.setRoles(roles);

        userRepository.save(existingUser);
    }
}

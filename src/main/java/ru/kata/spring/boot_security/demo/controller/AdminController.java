package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String adminPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("user", userService.findUserByName(userDetails.getUsername()));
        return "admin-page";
    }

    @GetMapping("/addNewUser")
    public String addUserPage() {
        return "add-new-user";
    }

    @GetMapping("/user")
    public String userPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("user", userService.findUserByName(userDetails.getUsername()));
        return "user-page";
    }

    @GetMapping("/api/user")
    @ResponseBody
    public User getAdminUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.findUserByName(userDetails.getUsername());
    }

    @GetMapping("/api/users")
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/api/roles")
    @ResponseBody
    public List<Role> getAllRoles() {
        return roleService.findAllRoles();
    }

    @PostMapping("/api/users")
    @ResponseBody
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userService.saveUserWithRole(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/api/users/{id}")
    @ResponseBody
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
        userService.updateUserWithRoles(id, user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/api/users/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
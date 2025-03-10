package com.javainhire.backend.controller;

import com.javainhire.backend.model.User;
import com.javainhire.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.saveUser(user);  // Ajouter un utilisateur
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);  // Récupérer un utilisateur par ID
    }

    // Ajoute d'autres endpoints selon les besoins
}

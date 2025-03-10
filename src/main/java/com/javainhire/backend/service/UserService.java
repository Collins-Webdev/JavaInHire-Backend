package com.javainhire.backend.service;

import com.javainhire.backend.model.User;
import com.javainhire.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);  // Enregistrer un utilisateur
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);  // Récupérer un utilisateur par ID
    }

    // Ajoute d'autres méthodes selon les besoins
}

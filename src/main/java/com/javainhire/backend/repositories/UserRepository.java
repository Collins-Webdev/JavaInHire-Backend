package com.javainhire.backend.repositories;

import com.javainhire.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Méthodes personnalisées si nécessaire
}
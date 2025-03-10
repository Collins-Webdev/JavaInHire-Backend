package com.javainhire.backend.repositories;

import com.javainhire.backend.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    // Méthodes personnalisées si nécessaire
}
package com.javainhire.backend.service;

import com.javainhire.backend.model.Application;
import com.javainhire.backend.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Application saveApplication(Application application) {
        return applicationRepository.save(application);  // Enregistrer une candidature
    }

    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id).orElse(null);  // Récupérer une candidature par ID
    }

    // Ajoute d'autres méthodes selon les besoins
}

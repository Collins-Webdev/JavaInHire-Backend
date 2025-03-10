package com.javainhire.backend.controller;

import com.javainhire.backend.model.Application;
import com.javainhire.backend.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public Application addApplication(@RequestBody Application application) {
        return applicationService.saveApplication(application);  // Ajouter une candidature
    }

    @GetMapping("/{id}")
    public Application getApplication(@PathVariable Long id) {
        return applicationService.getApplicationById(id);  // Récupérer une candidature par ID
    }

    // Ajoute d'autres endpoints selon les besoins
}

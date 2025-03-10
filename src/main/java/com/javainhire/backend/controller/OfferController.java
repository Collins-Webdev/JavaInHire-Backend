package com.javainhire.backend.controller;

import com.javainhire.backend.model.Offer;
import com.javainhire.backend.repositories.OfferRepository;
import com.javainhire.backend.service.RssFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

    @Autowired
    private RssFeedService rssFeedService;

    @Autowired
    private OfferRepository offerRepository;

    /**
     * Endpoint pour fetch et enregistrer les offres d'emploi.
     */
    @GetMapping("/fetch")
    public String fetchOffers() {
        rssFeedService.fetchAndSaveOffers();
        return "Offres fetchées et enregistrées avec succès !";
    }

    /**
     * Endpoint pour récupérer les offres paginées.
     */
    @GetMapping
    public Page<Offer> getOffers(@RequestParam(defaultValue = "0") int page) {
        return offerRepository.findAll(PageRequest.of(page, 10)); // 10 offres par page
    }
}
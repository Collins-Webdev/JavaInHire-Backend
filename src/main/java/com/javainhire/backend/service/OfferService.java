package com.javainhire.backend.service;

import com.javainhire.backend.model.Offer;
import com.javainhire.backend.repositories.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfferService {

    private final OfferRepository offerRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public Offer saveOffer(Offer offer) {
        return offerRepository.save(offer);  // Enregistrer une offre
    }

    public Offer getOfferById(Long id) {
        return offerRepository.findById(id).orElse(null);  // Récupérer une offre par ID
    }

    // Ajoute d'autres méthodes selon les besoins
}

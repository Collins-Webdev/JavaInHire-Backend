package com.javainhire.backend.repositories;

import com.javainhire.backend.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    boolean existsByLink(String link);
}
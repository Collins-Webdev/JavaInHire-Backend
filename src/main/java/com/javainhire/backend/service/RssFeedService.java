package com.javainhire.backend.service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.javainhire.backend.model.Offer;
import com.javainhire.backend.repositories.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class RssFeedService {

    @Autowired
    private OfferRepository offerRepository;

    // URLs des flux RSS
    private static final String[] RSS_FEEDS = {
            "https://weworkremotely.com/categories/remote-programming-jobs.rss",
            "https://remoteok.com/remote-java-jobs.rss",
            "https://empllo.com/feeds/remote-engineering-jobs.rss",
            "https://himalayas.app/jobs/rss",
            "https://remotive.com/remote-jobs/feed/software-dev",
            "https://jobicy.com/?feed=job_feed&job_categories=engineering",
            "https://jobicy.com/?feed=job_feed&job_categories=dev"
    };

    /**
     * Fetch et parse les flux RSS, puis enregistre les offres dans la base de données.
     */
    public void fetchAndSaveOffers() {
        for (String feedUrl : RSS_FEEDS) {
            try {
                // Fetch et parse le flux RSS
                URL url = new URL(feedUrl);
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(url));

                // Convertir les entrées RSS en objets Offer
                List<Offer> offers = parseFeedToOffers(feed);

                // Enregistrer les offres dans la base de données
                offerRepository.saveAll(offers);
            } catch (Exception e) {
                System.err.println("Erreur lors du fetch du flux RSS : " + feedUrl);
                e.printStackTrace();
            }
        }
    }

    /**
     * Convertit les entrées RSS en objets Offer.
     */
    private List<Offer> parseFeedToOffers(SyndFeed feed) {
        List<Offer> offers = new ArrayList<>();
        for (SyndEntry entry : feed.getEntries()) {
            String title = entry.getTitle();
            String description = entry.getDescription() != null ? entry.getDescription().getValue() : "";
            String link = entry.getLink();
            String source = feed.getTitle();

            // Extraire le niveau d'expérience du titre ou de la description
            String experienceLevel = extractExperienceLevel(title + " " + description);

            // Créer un objet Offer
            Offer offer = new Offer(title, description, experienceLevel, source, link);
            offers.add(offer);
        }
        return offers;
    }

    /**
     * Extrait le niveau d'expérience du texte (ex: "Junior", "Senior").
     */
    private String extractExperienceLevel(String text) {
        if (text.toLowerCase().contains("junior")) {
            return "Junior";
        } else if (text.toLowerCase().contains("senior")) {
            return "Senior";
        } else {
            return "Intermédiaire"; // Valeur par défaut
        }
    }

    /*
      Fetch les offres tous les trois jours.
*/
    @Scheduled(fixedRate = 3600000) // 259200000 3600000 ms = 1 heure
    public void fetchOffersScheduled() {
        fetchAndSaveOffers();
    }
}


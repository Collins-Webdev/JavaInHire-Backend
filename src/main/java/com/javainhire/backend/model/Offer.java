package com.javainhire.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerID;  // Identifiant unique de l'offre

    private String title;        // Titre de l'offre
    private String description;  // Description de l'offre
    private String experienceLevel;  // Niveau d'expérience requis
    private String source;      // Source de l'offre
    private String link;        // Lien vers l'offre
    private boolean isFavorite; // Est-ce une offre favorite ?
    private boolean isViewed;   // Est-ce que l'offre a été vue ?

    // Getters et setters
    public Long getOfferID() {
        return offerID;
    }

    public void setOfferID(Long offerID) {
        this.offerID = offerID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isViewed() {
        return isViewed;
    }

    public void setViewed(boolean viewed) {
        isViewed = viewed;
    }


    // Constructeur par défaut (requis par Hibernate)
    public Offer() {
    }

    // Constructeur avec arguments
    public Offer(String title, String description, String experienceLevel, String source, String link) {
        this.title = title;
        this.description = description;
        this.experienceLevel = experienceLevel;
        this.source = source;
        this.link = link;
        this.isFavorite = false;
        this.isViewed = false;
    }

}



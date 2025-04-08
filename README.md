# **JavaInHire - Plateforme d'Offres d'Emploi pour Développeurs Java**

## **📌 Table des Matières**
1. [**Description du Projet**](#-description-du-projet)
2. [**Technologies Utilisées**](#-technologies-utilisées)
3. [**Architecture du Projet**](#-architecture-du-projet)
4. [**Configuration Backend (Spring Boot)**](#-configuration-backend-spring-boot)
5. [**Base de Données (PostgreSQL)**](#-base-de-données-postgresql)
6. [**Authentification (AWS Cognito)**](#-authentification-aws-cognito)
7. [**Déploiement (Render & Netlify)**](#-déploiement-render--netlify)
8. [**API Endpoints**](#-api-endpoints)
9. [**Frontend (HTML/CSS/JS)**](#-frontend-htmlcssjs)
10. [**Tests et Bonnes Pratiques**](#-tests-et-bonnes-pratiques)
11. [**Améliorations Futures**](#-améliorations-futures)

---

## **🚀 Description du Projet**
**JavaInHire** est une plateforme spécialisée dans l'agrégation d'offres d'emploi pour développeurs Java (stagiaires, juniors et intermédiaires).  
**Fonctionnalités clés** :
- Récupération automatique d'offres depuis des flux RSS (RemoteOK, WeWorkRemotely, Empllo).
- Filtrage des offres par niveau d'expérience (Junior, Intermédiaire).
- Authentification sécurisée via AWS Cognito.
- Tableau de bord de suivi des candidatures.
- Hébergement 100% gratuit (Render + Netlify).

---

## **🛠 Technologies Utilisées**
| **Catégorie**       | **Technologies**                                                                 |
|---------------------|---------------------------------------------------------------------------------|
| **Backend**         | Java 21, Spring Boot 3.4, Spring Data JPA, Spring Security, AWS SDK for Java    |
| **Base de Données** | PostgreSQL (AWS RDS → Migration vers Render PostgreSQL)                         |
| **Authentification**| AWS Cognito (Gestion des utilisateurs, JWT)                                     |
| **Frontend**        | HTML5, CSS3, JavaScript (Vanilla), Font Awesome                                 |
| **Déploiement**     | Render (Backend + DB), Netlify (Frontend)                                       |
| **Outils**          | Gradle, Docker, Git, Postman, pgAdmin                                           |

---

## **🏗 Architecture du Projet**
```
java-inhire/
├── backend/                  # Code Spring Boot
│   ├── src/main/java/
│   │   ├── config/           # Config CORS, Security
│   │   ├── controller/       # Contrôleurs REST
│   │   ├── model/            # Entités JPA
│   │   ├── repository/       # Repositories Spring Data
│   │   ├── service/          # Logique métier
│   │   └── BackendApplication.java
│   ├── src/main/resources/
│   │   ├── application.properties # Config DB, JPA, Cognito
│   │   └── data.sql          # Données initiales (optionnel)
│   └── Dockerfile            # Configuration pour Render
├── frontend/                 # Code Frontend
│   ├── index.html            # Page vitrine
│   ├── dashboard.html        # Tableau de bord
│   ├── styles.css            # Styles globaux
│   └── script.js             # Logique JS (fetch API, etc.)
└── README.md                 # Documentation
```

---

## **⚙ Configuration Backend (Spring Boot)**

### **Fichier `application.properties`**
```properties
# PostgreSQL Config (Render)
spring.datasource.url=jdbc:postgresql://dpg-cn12345acn0c738f9vqg-a:5432/javainhire
spring.datasource.username=javainhire_user
spring.datasource.password=abc123def456

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Pool de connexions
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.hikari.maximum-pool-size=10

# AWS Cognito
aws.cognito.userPoolId=us-east-1_XXXXXXXXX
aws.cognito.clientId=XXXXXXXXXXXXXXXX
aws.cognito.region=us-east-1

# CORS (Autoriser Netlify)
spring.webflux.cors.allowed-origins=https://dashboard-javainhire.netlify.app
```

### **Classe Principale (`BackendApplication.java`)**
```java
@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling // Pour le fetch périodique des offres
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
```

---

## **🗃 Base de Données (PostgreSQL)**
### **Schéma des Tables**
```sql
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE offers (
    offer_id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    experience_level VARCHAR(50) NOT NULL,
    source VARCHAR(100) NOT NULL,
    link VARCHAR(200) NOT NULL,
    is_favorite BOOLEAN DEFAULT FALSE,
    is_viewed BOOLEAN DEFAULT FALSE
);

CREATE TABLE applications (
    application_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    offer_id INT REFERENCES offers(offer_id),
    status VARCHAR(50) NOT NULL,
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## **🔐 Authentification (AWS Cognito)
### **Configuration AWS SDK**
```java
@Configuration
public class CognitoConfig {
    @Value("${aws.cognito.region}")
    private String region;

    @Bean
    public CognitoIdentityProviderClient cognitoClient() {
        return CognitoIdentityProviderClient.builder()
                .region(Region.of(region))
                .build();
    }
}
```

### **Exemple d'Inscription**
```java
public void registerUser(String email, String password, String firstName, String lastName) {
    SignUpRequest request = SignUpRequest.builder()
            .clientId(clientId)
            .username(email)
            .password(password)
            .userAttributes(
                    AttributeType.builder().name("email").value(email).build(),
                    AttributeType.builder().name("given_name").value(firstName).build(),
                    AttributeType.builder().name("family_name").value(lastName).build()
            )
            .build();
    cognitoClient.signUp(request);
}
```

---

## **☁ Déploiement (Render & Netlify)**
### **Backend sur Render**
1. **Configuration Dockerfile** :
   ```dockerfile
   FROM openjdk:21-jdk-slim
   WORKDIR /app
   COPY build/libs/backend-0.0.1-SNAPSHOT.jar app.jar
   EXPOSE 8080
   ENTRYPOINT ["java", "-jar", "app.jar"]
   ```

2. **Variables d'environnement Render** :
   ```
   SPRING_DATASOURCE_URL=jdbc:postgresql://your-render-db-url
   SPRING_DATASOURCE_USERNAME=your-db-user
   SPRING_DATASOURCE_PASSWORD=your-db-password
   ```

### **Frontend sur Netlify**
- Déploiement continu depuis GitHub.
- URL publique : `https://dashboard-javainhire.netlify.app`

---

## **🔌 API Endpoints**
| **Endpoint**                | **Méthode** | **Description**                          |
|-----------------------------|------------|------------------------------------------|
| `/api/offers`               | GET        | Liste paginée des offres                 |
| `/api/offers/{id}/favorite` | POST       | Marquer une offre comme favorite         |
| `/api/auth/register`        | POST       | Inscription utilisateur                  |
| `/api/auth/login`           | POST       | Connexion utilisateur                    |

---

## **🎨 Frontend (HTML/CSS/JS)**
### **Structure Principale**
```html
<!-- dashboard.html -->
<div id="offers-list">
    <!-- Dynamiquement rempli par JS -->
</div>
<div class="pagination">
    <button id="prevPage">Précédent</button>
    <span id="pageInfo">Page 1</span>
    <button id="nextPage">Suivant</button>
</div>
```

### **Exemple de Fetch API**
```javascript
fetch("https://javainhire-backend.onrender.com/api/offers")
    .then(response => response.json())
    .then(data => {
        data.content.forEach(offer => {
            // Afficher chaque offre dans le DOM
        });
    });
```

---

## **🧪 Tests et Bonnes Pratiques**
### **Tests Spring Boot**
```java
@SpringBootTest
class OfferControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetOffers() throws Exception {
        mockMvc.perform(get("/api/offers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)));
    }
}
```

### **Bonnes Pratiques**
- Validation des entrées (`@Valid` dans les contrôleurs).
- Gestion des erreurs globales (`@ControllerAdvice`).
- Logging avec SLF4J.
- Sécurité : HTTPS, CORS restrictifs.

---

## **🚀 Améliorations Futures**
- **Notifications** : Alertes pour nouvelles offres.
- **Recherche avancée** : Filtres par compétences/salaire.
- **Intégration LinkedIn** : Postuler directement via l'API LinkedIn.
- **Monitoring** : Prometheus + Grafana pour les métriques.

---

## **📝 Licence**
**License**: MIT  
**Version**: 1.0.0  
**Dernière Mise à Jour**: Avril 2025

Ce document reste vivant et sera mis à jour en fonction de l'évolution du projet. Pour toute contribution, merci d'ouvrir une issue sur le dépôt GitHub.

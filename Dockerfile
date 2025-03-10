# Utiliser une image Java 21
FROM openjdk:21-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR de l'application
COPY build/libs/backend-0.0.1-SNAPSHOT.jar app.jar

# Copier le fichier application.properties
COPY src/main/resources/application.properties application.properties

# Exposer le port 8080
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
# Utiliser une image Gradle pour construire l'application
FROM gradle:8.4-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

# Utiliser une image Java 21 pour exécuter l'application
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/backend-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.properties application.properties

# Exposer le port 8080
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
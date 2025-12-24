FROM eclipse-temurin:21-jdk-alpine AS build
LABEL authors="dangphat"

COPY target/cards-0.0.1-SNAPSHOT.jar cards-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "cards-0.0.1-SNAPSHOT.jar"]
FROM eclipse-temurin:21-jdk-alpine AS build
LABEL authors="dangphat"

COPY target/loans-0.0.1-SNAPSHOT.jar loans-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "loans-0.0.1-SNAPSHOT.jar"]
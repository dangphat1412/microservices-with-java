FROM eclipse-temurin:21-jdk-alpine AS build
LABEL authors="dangphat"

RUN apk add --no-cache curl

COPY target/eurekaserver-0.0.1-SNAPSHOT.jar eurekaserver-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "eurekaserver-0.0.1-SNAPSHOT.jar"]
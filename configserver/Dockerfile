FROM eclipse-temurin:21-jdk-alpine AS build
LABEL authors="dangphat"

RUN apk add --no-cache curl

COPY target/configserver-0.0.1-SNAPSHOT.jar configserver-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "configserver-0.0.1-SNAPSHOT.jar"]
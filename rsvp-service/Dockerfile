FROM maven:3.9.9-eclipse-temurin-23-alpine
LABEL org.opencontainers.image.authors="hutchison.dev"
WORKDIR /rsvp-service/
COPY pom.xml /rsvp-service/
COPY src/ /rsvp-service/src
RUN mvn clean package -DskipTests
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "target/rsvp-service-1.0-SNAPSHOT.jar"]

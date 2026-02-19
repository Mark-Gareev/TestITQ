FROM maven:3.9.9-eclipse-temurin-22 AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn -B -q -e -C dependency:go-offline
COPY src ./src
RUN mvn -B clean package

FROM eclipse-temurin:22-jre-alpine

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=docker

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
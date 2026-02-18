FROM alpine/java:22-jdk
LABEL authors="Mark"
WORKDIR /app
COPY  /target/*.jar /app/*.jar
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/*.jar"]
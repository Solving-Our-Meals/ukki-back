FROM openjdk:17-jdk-slim
LABEL authors="admin"

COPY build/libs/ukki-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
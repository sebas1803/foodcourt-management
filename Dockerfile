# Build stage
FROM gradle:7.5-jdk17 AS BUILD
WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src

RUN gradle build --no-daemon

# Package stage
FROM openjdk:17
WORKDIR /app

COPY --from=BUILD /app/build/libs/ /app/

EXPOSE 8082

CMD ["java", "-jar", "power-up-arquetipo-0.0.1-SNAPSHOT.jar"]

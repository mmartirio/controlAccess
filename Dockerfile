# ============ BUILD STAGE ============
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app
COPY pom.xml /app
COPY src ./src
RUN mvn clean install

# ============ RUNTIME STAGE ============
FROM eclipse-temurin:21-jre-alpine


COPY --from=builder /app/target/controlAccess-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
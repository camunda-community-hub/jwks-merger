# ── Build stage ─────────────────────────────────────────────────────────────
FROM --platform=linux/amd64 maven:3-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn -q -DskipTests package

# ── Runtime stage ────────────────────────────────────────────────────────────
FROM --platform=linux/amd64 eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /build/target/jwks-merger-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

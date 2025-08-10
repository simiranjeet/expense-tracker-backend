# ---------- Stage 1: Build the app ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml first to cache dependencies
COPY pom.xml .

# Download dependencies (offline)
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build jar and skip compiling & running tests
RUN mvn clean package -Dmaven.test.skip=true

# ---------- Stage 2: Run the app ----------
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]

# ===============================
# Build Stage
# ===============================
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

# Copy Maven wrapper
COPY mvnw ./
COPY .mvn/ ./

# Make Maven wrapper executable
RUN chmod +x mvnw

# Copy pom.xml
COPY pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build Spring Boot application
RUN ./mvnw clean package -DskipTests


# ===============================
# Runtime Stage
# ===============================
FROM eclipse-temurin:25-jre

WORKDIR /app

# Copy generated JAR
COPY --from=build /app/target/*.jar app.jar

# Render default web service port
EXPOSE 10000

# Start application
ENTRYPOINT ["java", "-jar", "app.jar"]

# ===============================
# Build Stage
# ===============================
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

# Copy Maven wrapper
COPY mvnw .
COPY .mvn/ .mvn/
COPY pom.xml .

# Fix Windows CRLF -> Linux LF
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Copy source
COPY src ./src

# Build
RUN ./mvnw clean package -DskipTests


# ===============================
# Runtime Stage
# ===============================
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Render provides PORT
EXPOSE 10000

ENTRYPOINT ["java", "-jar", "app.jar"]

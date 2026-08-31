# ===============================
# Build Stage
# ===============================
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

COPY mvnw .
COPY .mvn/ .mvn/
COPY pom.xml .

RUN chmod +x mvnw

COPY src ./src

RUN ./mvnw clean package -DskipTests


# ===============================
# Runtime Stage
# ===============================
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 10000

ENTRYPOINT ["java", "-jar", "app.jar"]

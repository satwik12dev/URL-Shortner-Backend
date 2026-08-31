FROM eclipse-temurin:24-jdk AS build

WORKDIR /app

COPY mvnw .
COPY .mvn/ .mvn/
COPY pom.xml .

RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

COPY src ./src

RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:24-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 10000

ENTRYPOINT ["java", "-jar", "app.jar"]
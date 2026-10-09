# syntax=docker/dockerfile:1

FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /workspace

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw

RUN ./mvnw -B dependency:go-offline

COPY src src
RUN ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app

RUN addgroup -S app && adduser -S app -G app

COPY --from=build /workspace/target/*.jar app.jar

RUN chown app:app app.jar
USER app

EXPOSE 8080

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]

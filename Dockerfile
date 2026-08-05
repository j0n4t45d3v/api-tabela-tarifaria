FROM maven:3.9-amazoncorretto-21-alpine AS builder

WORKDIR /jar

COPY pom.xml /jar/pom.xml

RUN mvn -B -q -DskipTests dependency:go-offline

COPY src/ /jar/src/

RUN mvn -B -q package -DskipTests

FROM eclipse-temurin:21-jre-alpine-3.23

WORKDIR /app

COPY --from=builder /jar/target/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
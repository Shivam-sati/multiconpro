FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

VOLUME /tmp

ARG JAR_FILE=target/*.jar

COPY --from=build /workspace/app/${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
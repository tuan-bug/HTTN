#FROM openjdk:17
#RUN groupadd -r app && useradd -r -g app app
#USER app
#COPY target/be-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

#FROM eclipse-temurin:17-jdk-focal
#WORKDIR /app
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#RUN ./mvnw dependency:go-offline
#COPY src ./src
#CMD ["./mvnw", "spring-boot:run"]

FROM maven:3.8.5-openjdk-17 AS build
COPY .  .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/be-0.0.1-SNAPSHOT.jar be.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","be.jar"]


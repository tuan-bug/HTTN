#FROM openjdk:17
#RUN groupadd -r app && useradd -r -g app app
#USER app
##COPY target/be-0.0.1-SNAPSHOT.jar app.jar
#COPY --from=build /target/be-0.0.1-SNAPSHOT.jar be.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "/app.jar"]

#FROM eclipse-temurin:17-jdk-focal
#WORKDIR /app
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#RUN ./mvnw dependency:go-offline
#COPY src ./src
#CMD ["./mvnw", "spring-boot:run"]

#FROM maven:3.8.6-openjdk-17 AS build
#COPY .  .
#RUN mvn clean package -DskipTests
#
#FROM openjdk:17.0.1-jdk-slim
#COPY --from=build /target/be-0.0.1-SNAPSHOT.jar be.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","be.jar"]

#FROM maven:3.8.3-openjdk-17 AS build
#WORKDIR /app
#COPY . /app/
#RUN mvn clean package
#
##
## Package stage
##
#FROM openjdk:17-alpine
#WORKDIR /app
#COPY --from=build /app/target/*.jar /app/app.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","app.jar"]
#FROM maven:3.8.3-openjdk-17-slim AS build
#WORKDIR /app
#COPY . /app/
#RUN mvn clean package -DskipTests
#
#FROM openjdk:17.0.1-jdk-slim
#WORKDIR /app
#COPY --from=build /app/target/*.jar /app/app.jar
#ENTRYPOINT ["java","-jar","/app/app.jar"]
#


#FROM openjdk:17-jdk AS build
#WORKDIR /app
#COPY pom.xml .
#COPY src src
#
## Copy Maven wrapper
#COPY mvnw .
#COPY .mvn .mvn
#
## Set execution permission for the Maven wrapper
#RUN chmod +x ./mvnw
#RUN ./mvnw clean package -DskipTests
#
## Stage 2: Create the final Docker image using OpenJDK 19
#FROM openjdk:17-jdk
#VOLUME /tmp
#
## Copy the JAR from the build stage
#COPY --from=build /app/target/*.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]
#EXPOSE 8080
#
#FROM eclipse-temurin:17-jdk-alpine
#VOLUME /tmp
#COPY target/*.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]



#FROM ubuntu:latest AS build
#
#RUN apt-get update
#RUN apt-get install openjdk-17-jdk -y
#COPY . .
#
#RUN apt-get install maven -y
#RUN mvn clean install
#
#FROM openjdk:17-jdk-slim
#EXPOSE 8080
#COPY --from=build /target/be-0.0.1-SNAPSHOT.jar app.jar
#
#ENTRYPOINT ["java", "-jar", "app.jar"]




FROM ubuntu:latest AS build

RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven

WORKDIR /path/to/your/project
COPY . .

RUN mvn clean install

FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY --from=build /path/to/your/project/target/be-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

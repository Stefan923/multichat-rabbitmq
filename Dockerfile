FROM maven:3.6.3-openjdk-17 AS build-project
ADD . ./docker-multichat
WORKDIR /docker-multichat

RUN mvn clean install

FROM openjdk:17-oracle
EXPOSE 8080

COPY --from=build-project /docker-multichat/target/multichat-0.0.1-SNAPSHOT.jar ./docker-multichat.jar
CMD ["java", "-jar", "docker-multichat.jar"]
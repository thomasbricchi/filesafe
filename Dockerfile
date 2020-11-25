

FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

 #
 # Package stage
 #
FROM openjdk:11-jdk-slim
COPY --from=build /home/app/target/*.jar app.jar

WORKDIR /home/filesafe
RUN mkdir attachments


EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]

# Stage 1: Build the application
FROM maven:3.3-jdk-8 as build
WORKDIR /home/app
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn clean package

# Stage 2: Create the final image
FROM openjdk:8-jre-slim
WORKDIR /home/app
COPY --from=build /home/app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
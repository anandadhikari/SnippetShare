# Stage 1: Build the application
FROM gradle:7.6.4-jdk17 AS build
WORKDIR /app
COPY . /app
RUN gradle clean build

# Stage 2: Create a minimal JRE image and copy the built JAR file
FROM openjdk:17-slim
COPY --from=build /app/build/libs/backend-0.0.1-SNAPSHOT.jar /app.jar

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]

# Build stage
FROM maven:3.9.9-eclipse-temurin-24 AS build
WORKDIR /app
COPY . .
# First download dependencies to cache them
RUN mvn dependency:go-offline
# Then compile with annotation processing
RUN mvn clean compile -DskipTests
# Finally package
RUN mvn package -DskipTests

# Run stage
FROM eclipse-temurin:24
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

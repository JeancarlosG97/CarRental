# Use Maven + JDK to build the project
FROM maven:3.9.2-eclipse-temurin-17 AS build

# Set working directory inside the container
WORKDIR /app

# Copy only the pom first to cache dependencies
COPY pom.xml .

# Download dependencies (this layer is cached)
RUN mvn dependency:go-offline -B

# Copy the rest of the source code
COPY src ./src

# Package the application into a jar
RUN mvn clean package -DskipTests

# --- Runtime stage ---
FROM eclipse-temurin:17-jdk

# Set working directory in runtime image
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/CarRental-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
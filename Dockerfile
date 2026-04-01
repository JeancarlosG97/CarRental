# Use Java 17
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy jar file into container
COPY target/*.jar app.jar

# Expose port (Render will override with PORT env)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java","-jar","app.jar"]
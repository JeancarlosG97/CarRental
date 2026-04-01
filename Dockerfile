# Use Java 17 (working image)
FROM eclipse-temurin:17-jdk

# Copy jar file
COPY target/CarRental-0.0.1-SNAPSHOT.jar app.jar

# Run the app
ENTRYPOINT ["java","-jar","/app.jar"]
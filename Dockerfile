# Step 1: Use a base image with JDK 21
FROM eclipse-temurin:21-jdk-jammy

# Step 2: Set working directory
WORKDIR /app

# Step 3: Copy the JAR file into the container
COPY target/*.jar app.jar

# Step 4: Expose port 9000
EXPOSE 9000

# Step 5: Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]

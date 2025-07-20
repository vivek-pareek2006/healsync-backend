# Use official OpenJDK image for Java 23
FROM eclipse-temurin:23-jdk

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew gradlew.bat build.gradle settings.gradle /app/
COPY gradle /app/gradle

# Copy source code
COPY src /app/src

# Copy resources
COPY src/main/resources /app/src/main/resources

# Make gradlew executable
RUN chmod +x gradlew

# Build the application
RUN ./gradlew build -x test

# Use a minimal JRE image for running the app
FROM eclipse-temurin:23-jre
WORKDIR /app

# Copy built jar from builder image
COPY --from=0 /app/build/libs/*.jar /app/app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

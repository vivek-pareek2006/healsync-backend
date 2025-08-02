# Use official OpenJDK image for Java 17
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew gradlew.bat build.gradle settings.gradle ./
COPY gradle gradle

# Copy source code
COPY src src

# Make gradlew executable
RUN chmod +x gradlew

# Build the application
RUN ./gradlew build -x test

# Expose port 8080
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "build/libs/healsync-0.0.1-SNAPSHOT.jar"]

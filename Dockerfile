FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests
CMD ["java", "-jar", "target/drone-0.0.1-SNAPSHOT.jar"]

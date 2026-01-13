# Stage 1: Build với Maven
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy toàn bộ source code
COPY . .

# Build ứng dụng
RUN mvn clean package -DskipTests

# Stage 2: Chạy ứng dụng với JRE nhẹ
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy file JAR từ stage build
COPY --from=build /app/target/demoo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
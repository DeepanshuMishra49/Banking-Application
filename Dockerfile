# -------- STAGE 1: Build --------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# 1️⃣ Only pom first (cache layer)
COPY pom.xml .

# 2️⃣ Download dependencies (cached)
RUN mvn dependency:go-offline -B

# 3️⃣ Copy source code
COPY src ./src

# 4️⃣ Build jar
RUN mvn clean package -DskipTests


# -------- STAGE 2: Runtime --------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy only jar from builder
COPY --from=builder /app/target/*.jar app.jar

# Small optimization
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

EXPOSE 8088

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "app.jar"]

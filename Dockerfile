# Etapa 1: construir el WAR
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY . .

# Instalar Maven en Alpine
RUN apk add --no-cache maven

RUN mvn clean package -DskipTests

# Etapa 2: ejecutar el WAR
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.war app.war

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]

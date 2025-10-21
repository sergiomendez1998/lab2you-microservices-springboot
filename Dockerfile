# Etapa 1: construir el WAR
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Etapa 2: ejecutar el WAR
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.war app.war

# Usa el puerto dinámico de Render (no lo fijes en 9090)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.war"]

FROM eclipse-temurin:17-jdk-alpine
COPY target/final-project-backend.war final-project-backend.war
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "final-project-backend.war"]
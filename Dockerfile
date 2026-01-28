FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-Xmx384m", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
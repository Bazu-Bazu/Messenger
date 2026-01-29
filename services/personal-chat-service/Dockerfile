FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8083 6566

ENTRYPOINT ["java", "-jar", "app.jar"]
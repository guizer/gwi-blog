FROM openjdk:17.0.1-jdk-slim-bullseye

ARG JAR_FILE

COPY $JAR_FILE app.jar
CMD ["java", "-jar", "app.jar"]
